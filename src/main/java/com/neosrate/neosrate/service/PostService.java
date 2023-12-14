package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.PostAndLikeResponse;
import com.neosrate.neosrate.data.dto.PostDto;
import com.neosrate.neosrate.data.model.Community;
import com.neosrate.neosrate.data.model.Post;
import com.neosrate.neosrate.data.model.UserLike;
import com.neosrate.neosrate.repository.CommunityRepository;
import com.neosrate.neosrate.repository.PostRepository;
import com.neosrate.neosrate.repository.UserLikeRepository;
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

    @Autowired
    UserLikeRepository userLikeRepository;

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

    public ResponseEntity<?> getAllPost(Integer userId) {
        Iterable<Post> allPost = postRepository.findAll();
        List<UserLike> allUserLike = userLikeRepository.findByUserId(userId);

        if (!allPost.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POST NOT FOUND.");
        }

        var bodyResponse = new PostAndLikeResponse((List<Post>) allPost, allUserLike);
        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public ResponseEntity<?> getAllCommunityPost(String community) {
        List<Community> communityExists = communityRepository.findByName(community);

        if(communityExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CNF,COMMUNITY NOT FOUND.");
        }

        List<Post> posts = postRepository.findByCommunity(community);

        List<UserLike> communityUserLike = userLikeRepository.findByCommunity(community);

        var bodyResponse = new PostAndLikeResponse(posts, communityUserLike);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public ResponseEntity<?> getAllUserPost(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        List<UserLike> allUserLike = userLikeRepository.findByUserId(userId);

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POST NOT FOUND.");
        }
        var bodyResponse = new PostAndLikeResponse(posts, allUserLike);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public Integer likePost(Post post, Integer likeType, Integer previousLikeType) {
        System.out.println(post.getLikeCount());
        System.out.println(likeType);
        System.out.println(previousLikeType );
        if (likeType == 1) {
            if (previousLikeType == -1) {
                post.setDislikeCount(post.getDislikeCount() - 1);
                post.setLikeCount(post.getLikeCount() + 1);
            } else if (previousLikeType == 0) {
                post.setLikeCount(post.getLikeCount() + 1);
            }
            return 1;
        }

        else if (likeType == -1) {
            if (previousLikeType == 1) {
                post.setLikeCount(post.getLikeCount() - 1);
                post.setDislikeCount(post.getDislikeCount() + 1);
            } else if (previousLikeType == 0) {
                post.setDislikeCount(post.getDislikeCount() + 1);
            }
            return -1;
        }

        else if (likeType == 0) {
            if (previousLikeType == 1) {
                post.setLikeCount(post.getLikeCount() - 1);
            }
            else if (previousLikeType == -1) {
                post.setDislikeCount(post.getDislikeCount() - 1);
            }
            return 0;
        }

        return 0;
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

        if (postUpdateData.getTitle() != null) {
            post.setTitle(postUpdateData.getTitle());
        }

        if (postUpdateData.getText() != null) {
            post.setText(postUpdateData.getText());
        }

        if (postUpdateData.getLikeTrigger() != null) {
            UserLike userLike = userLikeRepository.findByPostIdAndUserId(postId, postUpdateData.getUserIdLiker());

            if (userLike != null) {
                System.out.println("sim");
                Integer likeType = likePost(post, postUpdateData.getLikeTrigger(), userLike.getLikeType());
                userLike.setLikeType(likeType);

                userLikeRepository.save(userLike);
            } else {
                UserLike newUserLike = new UserLike();
                newUserLike.setUserId(postUpdateData.getUserIdLiker());
                newUserLike.setPostId(post.getId());
                newUserLike.setCommunity(post.getCommunity());

                Integer likeType = likePost(post, postUpdateData.getLikeTrigger(), 0);

                newUserLike.setLikeType(likeType);

                userLikeRepository.save(newUserLike);
            }
        }

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
