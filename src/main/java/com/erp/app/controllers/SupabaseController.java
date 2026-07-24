package com.erp.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.app.DTO.ApiResponse;
import com.erp.app.entities.ArchiveMembers;
import com.erp.app.entities.Members;
import com.erp.app.service.SupabaseService;

@RestController
@RequestMapping("/supabase")
@EnableMethodSecurity
public class SupabaseController {

	@Autowired
	public SupabaseService serviceSupabase;

	@Autowired
	public GroqTestController translationService;


	@GetMapping("/get/members")
	public ResponseEntity<ApiResponse<List<Members>>> getAll() {
		List<Members> members = serviceSupabase.getAllMembers();
		return ResponseEntity.ok(ApiResponse.success("Members fetched successfully", members));
	}

	
	@GetMapping("/get/pastMember")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<ArchiveMembers>>> getArchiveList() {
		List<ArchiveMembers> archives = serviceSupabase.getArchives();
		return ResponseEntity.ok(ApiResponse.success("Archives fetched successfully", archives));
	}

	
	@GetMapping("/getUserById/{id}")
	public ResponseEntity<ApiResponse<Members>> getById(@PathVariable int id) {
		Members member = serviceSupabase.getMember(id);
		return ResponseEntity.ok(ApiResponse.success("Member found", member));
	}

	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Map<String, Object>>> addData(@RequestBody Map<String, Object> payload) {
		Map<String, Object> dataMap = translationService.translateAllFields(payload);
		serviceSupabase.addMember(dataMap);
		return ResponseEntity.ok(ApiResponse.success("Member added and translated", dataMap));
	}

	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<Map<String, Object>>> updateMember(@RequestBody Map<String, Object> payload) {
		Map<String, Object> dataMap = translationService.translateEditFields(payload);
		serviceSupabase.addMember(dataMap);
		return ResponseEntity.ok(ApiResponse.success("Member updated and translated", dataMap));
	}

	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable int id) {
		String result = serviceSupabase.deleteMember(id, "presentMember");
		return ResponseEntity.ok(ApiResponse.success(result, null));
	}

	
	@DeleteMapping("/delete/pastMember/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> deleteArchiveMember(@PathVariable int id) {
		String result = serviceSupabase.deleteMember(id, "pastMember");
		return ResponseEntity.ok(ApiResponse.success("Member Deleted", result));
	}
	
	
	@GetMapping("/get/userData/{email}")
	public ResponseEntity<ApiResponse<Members>> getUserData(@PathVariable String email){
		Members data = serviceSupabase.getUserData(email);
		return ResponseEntity.ok(ApiResponse.success("Login User Data", data));
	}
}
