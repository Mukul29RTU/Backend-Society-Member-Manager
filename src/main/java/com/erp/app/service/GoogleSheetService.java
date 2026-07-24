package com.erp.app.service;

import java.util.List;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.erp.app.entities.ArchiveMembers;
import com.erp.app.entities.Members;
import com.erp.app.repository.ArchiveMembersRepo;
import com.erp.app.repository.MembersRepo;
import com.erp.app.utility.MemberMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleSheetService {

    @Value("${google.sheets.web-app-url}")
    private String WEB_APP_URL;

    @Value("${google.sheets.archive-data-url}")
    private String ARCHIVE_DATA_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MembersRepo membersRepo;

    @Autowired
    private ArchiveMembersRepo archiveMembersRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getSheetDataPaged(int start, int limit) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(WEB_APP_URL)
                .queryParam("start", start)
                .queryParam("limit", limit)
                .encode()
                .toUriString();
        return restTemplate.getForObject(urlTemplate, String.class);
    }

    public String getSheetArchiveDataPaged(int start, int limit) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(ARCHIVE_DATA_URL)
                .queryParam("start", start)
                .queryParam("limit", limit)
                .encode()
                .toUriString();
        return restTemplate.getForObject(urlTemplate, String.class);
    }

    public String syncAllDataToSupabase() {
        return syncData(WEB_APP_URL, Members.class);
    }

    public String syncAllArchiveMembersDataToSupabase() {
        return syncData(ARCHIVE_DATA_URL, ArchiveMembers.class);
    }

    private <T extends com.erp.app.entities.BaseMember> String syncData(String baseUrl, Class<T> targetClass) {
        try {
            int start = 0;
            int limit = 100;
            boolean hasMore = true;
            int totalRecords = 0;

            while (hasMore) {
                String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("start", start)
                        .queryParam("limit", limit)
                        .encode()
                        .toUriString();

                String jsonData = restTemplate.getForObject(url, String.class);

                if (jsonData == null || jsonData.isEmpty())
                    break;

                List<Map<String, Object>> records = objectMapper.readValue(jsonData,
                        new TypeReference<List<Map<String, Object>>>() {
                        });

                if (records == null || records.isEmpty())
                    break;

                for (Map<String, Object> record : records) {
                    T member = targetClass.getDeclaredConstructor().newInstance();
                    MemberMapper.mapToEntity(record, member);
                    if (member instanceof Members) {
                        membersRepo.save((Members) member);
                    } else if (member instanceof ArchiveMembers) {
                        archiveMembersRepo.save((ArchiveMembers) member);
                    }
                    totalRecords++;
                }

                if (records.size() < limit)
                    hasMore = false;
                start += limit;
            }

            return "Successfully synced " + totalRecords + " records from Google Sheet to Supabase";
        } catch (Exception e) {
            throw new RuntimeException("Error syncing data from Google Sheets: " + e.getMessage());
        }
    }

    public String addRow(Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        return restTemplate.postForObject(WEB_APP_URL, request, String.class);
    }
}