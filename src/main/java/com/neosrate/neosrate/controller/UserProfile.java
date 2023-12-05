package com.neosrate.neosrate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userprofile")
public class UserProfile {
    public ResponseEntity updateUserProfile() {
        return ResponseEntity.ok().build();
    }
}
