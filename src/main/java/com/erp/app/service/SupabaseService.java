 package com.erp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.erp.app.controllers.GroqTestController;
import com.erp.app.entities.ArchiveMembers;
import com.erp.app.entities.Members;
import com.erp.app.exception.ResourceNotFoundException;
import com.erp.app.repository.ArchiveMembersRepo;
import com.erp.app.repository.SupabaseRepo;
import com.erp.app.utility.MemberMapper;

import jakarta.transaction.Transactional;

@Service
public class SupabaseService {

	@Autowired
	public GroqTestController translationService;

	@Autowired
	public SupabaseRepo repoSupabase;

	@Autowired
	public ArchiveMembersRepo repoArchive;


	public List<Members> getAllMembers() {
		return repoSupabase.findAll();
	}


	public List<ArchiveMembers> getArchives() {
		return repoArchive.findAll();
	}

	public Members getMember(int id) {
		return repoSupabase.findById(id)
				.orElseGet(() -> getArchiveOneMember(id));
	}

	public Members getArchiveOneMember(int id) {
		ArchiveMembers member = repoArchive.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member with ID " + id + " not found in archives."));
		Members memberData = new Members();
		BeanUtils.copyProperties(member, memberData);
		return memberData;
	}
	
	public Members getUserData(String email) {
		Members member = repoSupabase.findByEmail(email);
		return member;
	}

	@Transactional
	public void addMember(Map<String, Object> data) {
		Members member = MemberMapper.mapToEntity(data, new Members());
		repoSupabase.save(member);
	}

	@Transactional
	public String deleteMember(int id, String category) {
		if ("presentMember".equals(category)) {
			if (!repoSupabase.existsById(id)) {
                throw new ResourceNotFoundException("Member not found in active list.");
            }
			repoSupabase.deleteById(id);
		} else {
			if (!repoArchive.existsById(id)) {
                throw new ResourceNotFoundException("Member not found in archive list.");
            }
			repoArchive.deleteById(id);
		}

		return "Member Deleted";
	}

}
