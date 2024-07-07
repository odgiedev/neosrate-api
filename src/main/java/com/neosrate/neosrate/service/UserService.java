package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.user.UserDto;
import com.neosrate.neosrate.data.dto.user.UserSignInDto;
import com.neosrate.neosrate.data.enums.UserRole;
import com.neosrate.neosrate.data.model.User;
import com.neosrate.neosrate.data.model.UserProfile;
import com.neosrate.neosrate.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    final
    UserRepository userRepository;

    final
    UserProfileRepository userProfileRepository;

    final
    AuthenticationManager authenticationManager;

    final
    TokenService tokenService;

    final
    S3Service s3Service;

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

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, AuthenticationManager authenticationManager, TokenService tokenService, S3Service s3Service, CommunityRepository communityRepository, UserCommunityRepository userCommunityRepository, PostRepository postRepository, CommentRepository commentRepository, UserLikeRepository userLikeRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.s3Service = s3Service;
        this.communityRepository = communityRepository;
        this.userCommunityRepository = userCommunityRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userLikeRepository = userLikeRepository;
    }

    public ResponseEntity<?> createUser(UserDto userData) {
        Set<ConstraintViolation<UserDto>> val = validator.validate(userData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        var user = userRepository.findByEmailOrUsername(userData.getEmail(), userData.getUsername());

        if (!user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email or username already taken.");
        }

        userData.setPasswd(new BCryptPasswordEncoder().encode(userData.getPasswd()));

        userData.setRole(UserRole.USERR);

        User savedUser = userRepository.save(modelMapper.map(userData, User.class));

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        UserProfile userProfile = new UserProfile();

        userProfile.setUserId(savedUser.getId());
        userProfile.setUsername(savedUser.getUsername());
        userProfile.setCreatedAt(formattedDate);

        userProfileRepository.save(userProfile);

        s3Service.copyImage("default.png", "user"+savedUser.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body("Account created.");
    }

    public ResponseEntity<?> getUser(Integer id) {
         Optional<User> user = userRepository.findById(id);

         if(user.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
         }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<?> getRecentUser() {
        try {
            var recentUser = userRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity.status(HttpStatus.OK).body(recentUser);
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

//    public ResponseEntity<String> updateUser(Integer id, UserDto userUpdateData) {
//        Optional<User> userOptional = userRepository.findById(id);
//
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//        }
//
//        User user = userOptional.get();
//
//        user.setUsername(userUpdateData.getUsername());
//        user.setEmail(userUpdateData.getEmail());
//
//        userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.OK).body("User updated.");
//    }

    @Transactional
    public ResponseEntity<String> deleteUser(Integer ownerId) {
        Optional<User> userOptional = userRepository.findById(ownerId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        s3Service.deleteFile("user"+userOptional.get().getUsername());

        commentRepository.deleteAllByUserId(ownerId);
        postRepository.deleteAllByUserId(ownerId);
        userCommunityRepository.deleteAllByUserId(ownerId);
        userLikeRepository.deleteAllByUserId(ownerId);
        communityRepository.deleteAllByCreator(userOptional.get().getUsername());
        userProfileRepository.deleteByUserId(ownerId);

        userRepository.deleteById(ownerId);

        return ResponseEntity.status(HttpStatus.OK).body("User deleted.");
    }

    public ResponseEntity<?> signIn(UserSignInDto userData) {
        Set<ConstraintViolation<UserSignInDto>> val = validator.validate(userData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        UserDetails user = userRepository.findByEmail(userData.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email and/or password incorrect.");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPasswd());
        org.springframework.security.core.Authentication auth;
        try {
            auth = this.authenticationManager.authenticate(usernamePassword);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email and/or password incorrect.");
        }

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
