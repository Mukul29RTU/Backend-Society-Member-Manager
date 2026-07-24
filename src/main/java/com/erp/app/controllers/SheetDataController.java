package com.erp.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.erp.app.DTO.ApiResponse;
import com.erp.app.service.GoogleSheetService;

@RestController
@RequestMapping("/data")
@EnableMethodSecurity
public class SheetDataController {

	@Autowired
	public GroqTestController translationService;

	@Autowired
	public GoogleSheetService sheetsService;

	
	@GetMapping("/get")
	@PreAuthorize("hasRole('ADMIN')")
	public String readData(
			@RequestParam(defaultValue = "1") int start,
			@RequestParam(defaultValue = "20") int limit) {

		String response = sheetsService.getSheetDataPaged(start, limit);
		return response;
	}

	@PostMapping("/modify")
	@PreAuthorize("hasRole('ADMIN')")
	public String modifyData(@RequestBody Map<String, Object> payload) {
		String response = sheetsService.addRow(payload);
		return response;
	}
}
