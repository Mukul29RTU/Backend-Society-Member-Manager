package com.erp.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.app.service.GoogleSheetService;

@RestController
@RequestMapping("/api/members")
@EnableMethodSecurity
public class MembersController {

    @Autowired
    private GoogleSheetService googleSheetService;

    
    @GetMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public String syncMembersFromGoogleSheet() {
        String result = googleSheetService.syncAllDataToSupabase();
        return result;
    }

    
    @GetMapping("/sync/archiveMembers")
    @PreAuthorize("hasRole('ADMIN')")
    public String syncArchiveMembersFromGoogleSheet() {
        String result = googleSheetService.syncAllArchiveMembersDataToSupabase();
        return result;
    }
}
