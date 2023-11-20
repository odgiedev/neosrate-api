package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.CommunityDto;
import com.neosrate.neosrate.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    @Autowired
    CommunityService communityService;

    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody CommunityDto communityData) {
        return communityService.createCommunity(communityData);
    }
}
