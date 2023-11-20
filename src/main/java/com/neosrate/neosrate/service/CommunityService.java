package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.CommunityDto;
import com.neosrate.neosrate.data.model.Community;
import com.neosrate.neosrate.repository.CommunityRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommunityService {
    @Autowired
    CommunityRepository communityRepository;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public ResponseEntity<String> createCommunity(CommunityDto communityData) {
        Set<ConstraintViolation<CommunityDto>> val = validator.validate(communityData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        communityRepository.save(modelMapper.map(communityData, Community.class));

        return ResponseEntity.status(HttpStatus.OK).body("COMMUNITY CREATED.");
    }

//    public ResponseEntity<?> getAllPost() {
//        var allPost = postRepository.findAll();
//        return ResponseEntity.status(HttpStatus.OK).body(allPost);
//    }
//
//    public ResponseEntity<?> getPost(Integer postId) {
//        Optional<Post> post = postRepository.findById(postId);
//
//        if(post.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(post);
//    }
//
//    public ResponseEntity<?> getAllUserPost(Integer userId) {
//        List<Post> posts = postRepository.findByUserId(userId);
//
//        if (posts.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(posts);
//
//    }
//
//    public ResponseEntity<String> updatePost(Integer postId, PostDto postUpdateData) {
//        Set<ConstraintViolation<PostDto>> val = validator.validate(postUpdateData);
//
//        if (!val.isEmpty()) {
//            String err = val.iterator().next().getMessage();
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
//        }
//
//        Optional<Post> postOptional = postRepository.findById(postId);
//
//        if (postOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
//        }
//
//        Post post = postOptional.get();
//
//        post.setTitle(postUpdateData.getTitle());
//        post.setText(postUpdateData.getText());
//
//        postRepository.save(post);
//
//        return ResponseEntity.status(HttpStatus.OK).body("Post UPDATED.");
//    }
//
//    public ResponseEntity<String> deletePost(Integer postId) {
//        Optional<Post> postOptional = postRepository.findById(postId);
//
//        if (postOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
//        }
//
//        postRepository.deleteById(postId);
//        return ResponseEntity.status(HttpStatus.OK).body("Post DELETED.");
//    }
}
