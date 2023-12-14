package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.user.UserProfileDto;
import com.neosrate.neosrate.data.model.User;
import com.neosrate.neosrate.data.model.UserProfile;
import com.neosrate.neosrate.repository.UserProfileRepository;
import com.neosrate.neosrate.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {
    @Autowired
    UserProfileRepository userProfileRepository;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserId(userId);

        if (userProfileOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        UserProfile user = userProfileOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Integer userId, @RequestBody @Valid UserProfileDto userUpdateData) {
        var optionalUserProfile = userProfileRepository.findByUserId(userId);

        if (!optionalUserProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        var user = optionalUserProfile.get();

        if (userUpdateData.getBio() != null) {
            user.setBio(userUpdateData.getBio());
        }
        if (userUpdateData.getPfpPath() != null) {
            user.setPfpPath(userUpdateData.getPfpPath());
        }
        if (userUpdateData.getCommunityOwner() != null) {
            user.setCommunityOwner(userUpdateData.getCommunityOwner());
        }
        if (userUpdateData.getCommunityParticipant() != null) {
            user.setCommunityParticipant(userUpdateData.getCommunityParticipant());
        }

        userProfileRepository.save(user);

        return ResponseEntity.ok().body("USER UPDATED");
    }
}
