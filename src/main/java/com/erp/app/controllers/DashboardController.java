package com.erp.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.app.DTO.ApiResponse;
import com.erp.app.DTO.DashboardStatsDTO;
import com.erp.app.entities.Members;
import com.erp.app.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@EnableMethodSecurity
public class DashboardController {

	@Autowired
	public DashboardService dashboardService;

	@GetMapping("/memberCount")
	public ResponseEntity<ApiResponse<DashboardStatsDTO>> getStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard data fetched", stats));
    }
	
	@GetMapping("/recentMembers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<Members>>> getRecentRegistration(){
		List<Members> result = dashboardService.getRecentMembers();
		
		return ResponseEntity.ok(ApiResponse.success("Last Three Members",result));
	}
	
	@GetMapping("/wardWiseCount")
    public ResponseEntity<Map<String, Long>> getWardWiseCount() {
        Map<String, Long> stats = dashboardService.getWardWiseCounts();
        return ResponseEntity.ok(stats);
    }
}
