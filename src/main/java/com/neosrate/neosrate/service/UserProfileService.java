package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.user.UserProfileDto;
import com.neosrate.neosrate.data.model.UserProfile;
import com.neosrate.neosrate.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserProfileService {
    UserProfileRepository userProfileRepository;

    S3Service s3Service;

    public UserProfileService(UserProfileRepository userProfileRepository, S3Service s3Service) {
        this.userProfileRepository = userProfileRepository;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> getUserProfile(String username) {
        UserProfile user = userProfileRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER PROFILE NOT FOUND");
        }


        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<String> updateUserProfile(Integer userId, UserProfileDto userUpdateData) {
        var optionalUserProfile = userProfileRepository.findByUserId(userId);

        if (optionalUserProfile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        var user = optionalUserProfile.get();

        if (userUpdateData.getBio() != null) {
            user.setBio(userUpdateData.getBio().trim().lines()
                    .map(line -> line.trim().replaceAll("\\s+", " "))
                    .filter(line -> !line.isEmpty())
                    .reduce((acc, line) -> acc + "\n" + line)
                    .orElse(""));
        }

        userProfileRepository.save(user);

        return ResponseEntity.ok().body("USER UPDATED");
    }

    public ResponseEntity<?> updatePfp(MultipartFile file, Integer userId) throws IOException {
        Optional<UserProfile> userExist = userProfileRepository.findByUserId(userId);

        if (userExist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        UserProfile user = userExist.get();

        var uploadedFile = s3Service.uploadPicture("user"+user.getUsername(), file);

        if (!uploadedFile) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while uploading.");
        }

//        user.setPfpName("user"+user.getUsername());
        userProfileRepository.save(user);

        return ResponseEntity.ok().body("Profile picture updated.");
    }
}
