package com.erp.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.app.DTO.DashboardStatsDTO;
import com.erp.app.entities.Members;
import com.erp.app.repository.ArchiveMembersRepo;
import com.erp.app.repository.SupabaseRepo;

@Service
public class DashboardService {

	@Autowired
	public SupabaseRepo repoSupabase;
	
	@Autowired
	public ArchiveMembersRepo repoArchive;
	
	public DashboardStatsDTO getDashboardStats() {
		return DashboardStatsDTO.builder()
                .totalMembers(repoSupabase.getTotalMemberCount())
                .totalWards(repoSupabase.getTotalWard())
                .totalArchived(repoArchive.getTotalMemberCount())
                .build();
    }
	
	
	public List<Members> getRecentMembers() {
	    return repoSupabase.findLastThreeMembersNative();
	}
	
	public Map<String, Long> getWardWiseCounts() {
	    List<Object[]> results = repoSupabase.getWardWiseCountRaw();
	    Map<String, Long> wardCounts = new HashMap<>();
	    
	    for (Object[] result : results) {
	        String wardName = (String) result[0];
	        Long count = (Long) result[1];
	        wardCounts.put(wardName, count);
	    }
	    return wardCounts;
	}
}
