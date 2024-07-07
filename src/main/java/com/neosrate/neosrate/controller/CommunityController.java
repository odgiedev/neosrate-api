package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.Community.CommunityDto;
import com.neosrate.neosrate.data.dto.Community.CommunityUpdateDto;
import com.neosrate.neosrate.data.dto.UserCommunityDto;
import com.neosrate.neosrate.repository.CommunityRepository;
import com.neosrate.neosrate.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    final
    CommunityService communityService;

    final
    CommunityRepository communityRepository;

    public CommunityController(CommunityService communityService, CommunityRepository communityRepository) {
        this.communityService = communityService;
        this.communityRepository = communityRepository;
    }

    @PostMapping("/update/communitypic/{communityName}/{userId}")
    public ResponseEntity<?> updateCommunityPic(@RequestParam("file") MultipartFile file, @PathVariable String communityName) throws IOException {
        return communityService.updateCommunityPic(file, communityName);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createCommunity(@RequestBody CommunityDto communityData, @PathVariable Integer userId) {
        return communityService.createCommunity(communityData, userId);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateCommunity(@RequestBody CommunityUpdateDto communityUpdateData) {
        return communityService.updateCommunity(communityUpdateData);
    }

    @GetMapping("/get/community/{community}")
    public ResponseEntity<?> getCommunity(@PathVariable String community) {
        return communityService.getCommunity(community);
    }

    @GetMapping("/get/all/community")
    public ResponseEntity<?> getAllCommunity() {
        return communityService.getAllCommunities();
    }

    @GetMapping("/get/participants/{community}")
    public ResponseEntity<?> getCommunityParticipants(@PathVariable String community) {
        return communityService.getCommunityParticipants(community);
    }

    @GetMapping("/get/owner/{username}")
    public ResponseEntity<?> getCommunityOwner(@PathVariable String username) {
        return communityService.getCommunityOwner(username);
    }

    @PostMapping("/join/{userId}")
    public ResponseEntity<?> joinCommunity(@RequestBody UserCommunityDto userCommunityData, @PathVariable Integer userId){
        return communityService.joinCommunity(userCommunityData, userId);
    }

    @DeleteMapping("/delete/{communityName}/{ownerId}")
    public ResponseEntity<?> deleteCommunity(@PathVariable String communityName) {
        return communityService.deleteCommunity(communityName);
    }
}
