package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.PostDto;
import com.neosrate.neosrate.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ShowController {
    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDto showData) {
        return postService.createPost(showData);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllPost() {
        return postService.getAllPost();
    }

    @GetMapping("/get/{showId}")
    public ResponseEntity<?> getPost(@PathVariable Integer showId) {
        return postService.getPost(showId);
    }

    @GetMapping("/get/all/{userId}")
    public ResponseEntity<?> getAllUserPost(@PathVariable Integer userId) {
        return postService.getAllUserPost(userId);
    }

    @PutMapping("/update/{showId}")
    public ResponseEntity<String> updatePost(@PathVariable Integer showId, @RequestBody PostDto showUpdateData) {
        return postService.updatePost(showId, showUpdateData);
    }

    @DeleteMapping("/delete/{showId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer showId) {
        return postService.deletePost(showId);
    }
}
