package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.PostDto;
import com.neosrate.neosrate.data.model.Community;
import com.neosrate.neosrate.data.model.Post;
import com.neosrate.neosrate.repository.CommunityRepository;
import com.neosrate.neosrate.repository.PostRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ResponseBody
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommunityRepository communityRepository;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public ResponseEntity<String> createPost(PostDto postData) {
        Set<ConstraintViolation<PostDto>> val = validator.validate(postData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        postRepository.save(modelMapper.map(postData, Post.class));

        return ResponseEntity.status(HttpStatus.OK).body("Post CREATED.");
    }

    public ResponseEntity<?> getAllPost() {
        var allPost = postRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allPost);
    }

    public ResponseEntity<?> getAllCommunityPost(String community) {
        List<Community> communityExists = communityRepository.findByName(community);

        if(communityExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("COMMUNITY NOT FOUND.22");
        }

        List<Post> communities = postRepository.findByCommunity(community);

        return ResponseEntity.status(HttpStatus.OK).body(communities);
    }

    public ResponseEntity<?> getAllUserPost(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(posts);

    }

    public ResponseEntity<String> updatePost(Integer postId, PostDto postUpdateData) {
        Set<ConstraintViolation<PostDto>> val = validator.validate(postUpdateData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
        }

        Post post = postOptional.get();

        post.setTitle(postUpdateData.getTitle());
        post.setText(postUpdateData.getText());

        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.OK).body("Post UPDATED.");
    }

    public ResponseEntity<String> deletePost(Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
        }

        postRepository.deleteById(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post DELETED.");
    }
}
