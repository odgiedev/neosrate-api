package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.user.UserProfileDto;
import com.neosrate.neosrate.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {
    final
    UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/update/pfp/{userId}")
    public ResponseEntity<?> updatePfp(@RequestParam("file") MultipartFile file, @PathVariable Integer userId) throws IOException {
        return userProfileService.updatePfp(file, userId);
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        return userProfileService.getUserProfile(username);
    }

    @PutMapping("/update/{userId}/{ownerId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Integer userId, @RequestBody @Valid UserProfileDto userUpdateData) {
        return userProfileService.updateUserProfile(userId, userUpdateData);
    }
}
