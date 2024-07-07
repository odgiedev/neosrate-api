package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.Community.CommunityDto;
import com.neosrate.neosrate.data.dto.Community.CommunityUpdateDto;
import com.neosrate.neosrate.data.dto.UserCommunityDto;
import com.neosrate.neosrate.data.model.Community;
import com.neosrate.neosrate.data.model.UserCommunity;
import com.neosrate.neosrate.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommunityService {
    final
    CommunityRepository communityRepository;

    final
    UserCommunityRepository userCommunityRepository;

    final
    PostRepository postRepository;

    final
    CommentRepository commentRepository;

    final
    UserLikeRepository userLikeRepository;

    final
    S3Service s3Service;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public CommunityService(CommunityRepository communityRepository, UserCommunityRepository userCommunityRepository, PostRepository postRepository, CommentRepository commentRepository, UserLikeRepository userLikeRepository, S3Service s3Service) {
        this.communityRepository = communityRepository;
        this.userCommunityRepository = userCommunityRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userLikeRepository = userLikeRepository;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> getAllCommunities() {
        Iterable<Community> allCommunities = communityRepository.findAll();

        if (!allCommunities.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(allCommunities);
    }

    public ResponseEntity<?> getCommunity(String community) {
        Community comm = communityRepository.findByName(community);

        if(comm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(comm);
    }

    public ResponseEntity<String> createCommunity(CommunityDto communityData, Integer userId) {
        Set<ConstraintViolation<CommunityDto>> val = validator.validate(communityData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        Community communityExist = communityRepository.findByName(communityData.getName());

        if (communityExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("COMMUNITY ALREADY EXIST.");
        }

        java.time.LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        communityData.setCreatedAt(formattedDate);

        communityRepository.save(modelMapper.map(communityData, Community.class));

        var userCommunityData = new UserCommunityDto();
        userCommunityData.setUserId(userId);
        userCommunityData.setUsername(communityData.getCreator());
        userCommunityData.setCommunity(communityData.getName());

        joinCommunity(userCommunityData, userId);

        s3Service.copyImage("community_default.png", "community"+communityData.getName());

        return ResponseEntity.status(HttpStatus.OK).body("COMMUNITY CREATED.");
    }

    public ResponseEntity<?> updateCommunity(CommunityUpdateDto communityUpdateData) {
        Set<ConstraintViolation<CommunityUpdateDto>> val = validator.validate(communityUpdateData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        Optional<Community> communityExist = communityRepository.findById(communityUpdateData.getCommunityId());

        if (communityExist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        Community community = communityExist.get();

        if (communityUpdateData.getDescription() != null) {
            community.setDescription(communityUpdateData.getDescription().trim().replaceAll("(?m)^(\\s*\\n){2,}", "\n"));
        }

        communityRepository.save(community);

        return ResponseEntity.status(HttpStatus.OK).body("Community updated.");
    }

    @Transactional
    public ResponseEntity<?> joinCommunity(UserCommunityDto userCommunityData, Integer userId) {
        Set<ConstraintViolation<UserCommunityDto>> val = validator.validate(userCommunityData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        Community communityExist = communityRepository.findByName(userCommunityData.getCommunity());

        if (communityExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        UserCommunity userCommunityExist = userCommunityRepository.findByUserIdAndCommunity(userId, userCommunityData.getCommunity());

        userCommunityData.setUserId(userId);

        if (userCommunityExist != null) {
            userCommunityRepository.deleteByUserIdAndCommunity(userId, userCommunityData.getCommunity());

            return ResponseEntity.status(HttpStatus.OK).body("UnJoined.");
        }

        userCommunityRepository.save(modelMapper.map(userCommunityData, UserCommunity.class));

        return ResponseEntity.status(HttpStatus.OK).body("Joined.");
    }

    public ResponseEntity<?> getCommunityParticipants(String community) {
        Community communityExist = communityRepository.findByName(community);

        if (communityExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        List<UserCommunity> participants = userCommunityRepository.findByCommunity(community);

        return ResponseEntity.status(HttpStatus.OK).body(participants);
    }

    public ResponseEntity<?> getCommunityOwner(String username) {
        List<Community> communities = communityRepository.findByCreator(username);

        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    public ResponseEntity<?> updateCommunityPic(MultipartFile file, String communityName) throws IOException {
        Community community = communityRepository.findByName(communityName);

        if (community == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        var uploadedFile = s3Service.uploadPicture("community"+communityName, file);

        if (!uploadedFile) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while uploading.");
        }

//        community.setCommunityPic("community"+communityName);

        communityRepository.save(community);

        return ResponseEntity.ok().body("Community picture updated.");
    }

    @Transactional
    public ResponseEntity<?> deleteCommunity(String communityName) {
        Community community = communityRepository.findByName(communityName);

        if (community == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community not found.");
        }

        s3Service.deleteFile("community"+communityName);

        commentRepository.deleteAllByCommunity(communityName);
        postRepository.deleteAllByCommunity(communityName);
        userCommunityRepository.deleteAllByCommunity(communityName);
        userLikeRepository.deleteAllByCommunity(communityName);

        communityRepository.deleteById(community.getId());

        return ResponseEntity.status(HttpStatus.OK).body("Community deleted.");
    }
}
