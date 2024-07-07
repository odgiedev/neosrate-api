package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.PostResponse;
import com.neosrate.neosrate.data.dto.post.PostCreateDto;
import com.neosrate.neosrate.data.dto.post.PostUpdateDto;
import com.neosrate.neosrate.data.model.*;
import com.neosrate.neosrate.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@ResponseBody
public class PostService {
    final
    UserRepository userRepository;

    final
    PostRepository postRepository;

    final
    CommunityRepository communityRepository;

    final
    UserCommunityRepository userCommunityRepository;

    final
    UserLikeRepository userLikeRepository;

    final
    CommentRepository commentRepository;

    final
    S3Service s3Service;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public PostService(UserRepository userRepository, PostRepository postRepository, CommunityRepository communityRepository, UserCommunityRepository userCommunityRepository, UserLikeRepository userLikeRepository, CommentRepository commentRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
        this.userCommunityRepository = userCommunityRepository;
        this.userLikeRepository = userLikeRepository;
        this.commentRepository = commentRepository;
        this.s3Service = s3Service;
    }

    public ResponseEntity<String> createPost(Integer userId, PostCreateDto postData, MultipartFile file) throws IOException {
        Set<ConstraintViolation<PostCreateDto>> val = validator.validate(postData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        if (file != null) {
            postData.setText(null);
            String fileName = LocalDateTime.now() + file.getOriginalFilename();
            postData.setFilePath(fileName);

            boolean uploadedFile = s3Service.uploadFile(fileName, file);

            if (!uploadedFile) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Only pictures, gifs and mp4.");
            }
        }

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        postData.setCreatedAt(formattedDate);

        postData.setUserId(userId);

        postData.setId(null);

        postRepository.save(modelMapper.map(postData, Post.class));

        return ResponseEntity.status(HttpStatus.OK).body("Post CREATED.");
    }

    public ResponseEntity<?> getAllPost(Integer userId, Integer maxPerPage) {
        Page<Post> allPost = postRepository.findAll(PageRequest.of(0, maxPerPage, Sort.by(Sort.Direction.DESC, "id")));

        if (allPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POST NOT FOUND.");
        }

        List<UserLike> allUserLike = userLikeRepository.findByUserId(userId);

        Iterable<Comment> allComment = commentRepository.findAll();

        long totalPosts = postRepository.count();

        var bodyResponse = new PostResponse(allPost, allUserLike, allComment, totalPosts);
        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public ResponseEntity<?> getRecentPost() {
        try {
            var recentPost = postRepository.findAll(PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity.status(HttpStatus.OK).body(recentPost);
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    public ResponseEntity<?> getAllCommunityPost(String community, Integer userId, Integer maxPerPage) {
        Community communityExists = communityRepository.findByName(community);

        if(communityExists == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CNF,COMMUNITY NOT FOUND.");
        }

        List<Post> posts = postRepository.findByCommunity(community, PageRequest.of(0, maxPerPage, Sort.by(Sort.Direction.DESC, "id")));

        List<UserLike> communityUserLike = userLikeRepository.findByCommunityAndUserId(community, userId);

        List<Comment> allComment = commentRepository.findByCommunity(community);

        long totalPosts = postRepository.countByCommunity(community);

        var bodyResponse = new PostResponse(posts, communityUserLike, allComment, totalPosts);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public ResponseEntity<?> getAllUserPost(Integer loggedUserId, String username, Integer maxPerPage) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UNF,USER NOT FOUND.");
        }

        var posts = postRepository.findByUsername(username, PageRequest.of(0, maxPerPage, Sort.by(Sort.Direction.DESC, "id")));

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PNF,POST NOT FOUND.");
        }

        List<UserLike> allUserLike = userLikeRepository.findByUserId(loggedUserId);

        List<Comment> allComment = commentRepository.findByCreator(username);

        long totalPosts = postRepository.countByUsername(username);

        var bodyResponse = new PostResponse(posts, allUserLike, allComment, totalPosts);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public Integer likePost(Post post, Integer likeType, Integer previousLikeType) {
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

    public ResponseEntity<String> updatePost(Integer postId, PostUpdateDto postUpdateData, MultipartFile file) throws IOException {
        Set<ConstraintViolation<PostUpdateDto>> val = validator.validate(postUpdateData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
        }

        Post post = postOptional.get();

        if (postUpdateData.getTitle() != null && !Objects.equals(postUpdateData.getTitle().trim(), "")) {
            post.setTitle(postUpdateData.getTitle().trim().lines()
                    .map(line -> line.trim().replaceAll("\\s+", " "))
                    .filter(line -> !line.isEmpty())
                    .reduce((acc, line) -> acc + "\n" + line)
                    .orElse(""));
        }

        if (postUpdateData.getText() != null) {
            post.setText(postUpdateData.getText().trim().lines()
                    .map(line -> line.trim().replaceAll("\\s+", " "))
                    .filter(line -> !line.isEmpty())
                    .reduce((acc, line) -> acc + "\n" + line)
                    .orElse(""));
        }

        if (file != null) {
            s3Service.deleteFile(post.getFilePath());

            String fileName = LocalDateTime.now() + file.getOriginalFilename();

            post.setFilePath(fileName);

            boolean uploadedFile = s3Service.uploadFile(fileName, file);

            if (!uploadedFile) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Only pictures, gifs and mp4.");
            }
        }

        if (postUpdateData.getLikeTrigger() != null) {
            UserLike userLike = userLikeRepository.findByPostIdAndUserId(postId, postUpdateData.getUserIdLiker());

            if (userLike != null) {
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
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        post.setCreatedAt(formattedDate);

        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.OK).body("Post UPDATED.");
    }

    @Transactional
    public ResponseEntity<String> deletePost(Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post NOT FOUND.");
        }

        userLikeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);

        postRepository.deleteById(postId);

        return ResponseEntity.status(HttpStatus.OK).body("Post DELETED.");
    }

    public ResponseEntity<?> searchQuery(Integer userId, String searchQuery, Integer maxPerPage) {
        var allPost = postRepository.findByTitleContains(searchQuery, PageRequest.of(0, maxPerPage, Sort.by(Sort.Direction.DESC, "id")));

        if (allPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Any post found.");
        }

        List<UserLike> allUserLike = userLikeRepository.findByUserId(userId);

        Iterable<Comment> allComment = commentRepository.findAll();

        long totalPosts = postRepository.countByTitleContains(searchQuery);

        var bodyResponse = new PostResponse(allPost, allUserLike, allComment, totalPosts);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }

    public ResponseEntity<?> getPostUserJoinedCommunity(Integer userId, Integer maxPerPage) {
        var userJoinedCommunities= userCommunityRepository.findByUserId(userId);

        List<Post> allCommunityPost = new ArrayList<>();
        List<Comment> allCommunityComment = new ArrayList<>();

        userJoinedCommunities.forEach(community -> {
            var communityPost= postRepository.findAllByCommunity(community.getCommunity());
            var communityComment= commentRepository.findByCommunity(community.getCommunity());

            allCommunityPost.addAll(communityPost);
            allCommunityComment.addAll(communityComment);
        });

        allCommunityPost.sort((p1, p2) -> Integer.compare(p2.getId(), p1.getId()));

        long totalPosts = allCommunityPost.size();

        List<Post> pagedAllPost = allCommunityPost.subList(0, Math.min(allCommunityPost.size(), maxPerPage));

        List<UserLike> allUserLike = userLikeRepository.findByUserId(userId);

        var bodyResponse = new PostResponse(pagedAllPost, allUserLike, allCommunityComment, totalPosts);

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }
}
