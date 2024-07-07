package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.post.PostCreateDto;
import com.neosrate.neosrate.data.dto.post.PostUpdateDto;
import com.neosrate.neosrate.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/post")
public class PostController {
    final
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/create/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createPost(@PathVariable Integer userId, @RequestPart("data") PostCreateDto postData, @Nullable @RequestPart("file") MultipartFile file) throws IOException {
        return postService.createPost(userId, postData, file);
    }

    @GetMapping("/get/all/{maxPerPage}/{userId}")
    public ResponseEntity<?> getAllPost(@PathVariable Integer userId, @PathVariable Integer maxPerPage) {
        return postService.getAllPost(userId, maxPerPage);
    }

    @GetMapping("/get/recent")
    public ResponseEntity<?> getRecentPost() {
        return postService.getRecentPost();
    }

    @GetMapping("/get/all/community/{maxPerPage}/{community}/{userId}")
    public ResponseEntity<?> getAllCommunityPost(@PathVariable String community, @PathVariable Integer userId, @PathVariable Integer maxPerPage) {
        return postService.getAllCommunityPost(community, userId, maxPerPage);
    }

    @GetMapping("/get/all/user/{maxPerPage}/{userId}/{username}")
    public ResponseEntity<?> getAllUserPost(@PathVariable Integer userId, @PathVariable String username, @PathVariable Integer maxPerPage) {
        return postService.getAllUserPost(userId, username, maxPerPage);
    }

    @PutMapping("/update/{postId}/{ownerId}")
    public ResponseEntity<String> updatePost(@PathVariable Integer postId, @RequestPart("data") PostUpdateDto postUpdateData, @Nullable @RequestPart("file") MultipartFile file) throws IOException {
        return postService.updatePost(postId, postUpdateData, file);
    }

    @DeleteMapping("/delete/{postId}/{ownerId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        return postService.deletePost(postId);
    }

    @GetMapping("/search/{maxPerPage}/{userId}/{searchQuery}")
    public ResponseEntity<?> searchQuery(@PathVariable Integer userId, @PathVariable String searchQuery, @PathVariable Integer maxPerPage) {
        return postService.searchQuery(userId, searchQuery, maxPerPage);
    }

    @GetMapping("/get/joined/{maxPerPage}/{userId}")
    public ResponseEntity<?> getUserJoinedCommunity(@PathVariable Integer userId, @PathVariable Integer maxPerPage) {
        return postService.getPostUserJoinedCommunity(userId, maxPerPage);
    }
}
