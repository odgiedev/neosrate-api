package com.neosrate.neosrate.service;

import com.neosrate.neosrate.data.dto.user.UserDto;
import com.neosrate.neosrate.data.dto.user.UserSignInDto;
import com.neosrate.neosrate.data.model.User;
import com.neosrate.neosrate.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public ResponseEntity<?> createUser(UserDto userData) {
        Set<ConstraintViolation<UserDto>> val = validator.validate(userData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        var user = userRepository.findByEmail(userData.getEmail());

        if (!user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("EMAIL ALREADY REGISTERED.");
        }

        userRepository.save(modelMapper.map(userData, User.class));

        return ResponseEntity.status(HttpStatus.OK).body("USER CREATED.");
    }

    public ResponseEntity<?> getUser(Integer id) {
         Optional<User> user = userRepository.findById(id);

         if(user.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND.");
         }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<String> updateUser(Integer id, UserDto userUpdateData) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        User user = userOptional.get();

        user.setUsername(userUpdateData.getUsername());
        user.setEmail(userUpdateData.getEmail());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("USER UPDATED");
    }

    public ResponseEntity<String> deleteUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER NOT FOUND");
        }

        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER DELETED");
    }

    public ResponseEntity<?> signIn(UserSignInDto userData) {
        Set<ConstraintViolation<UserSignInDto>> val = validator.validate(userData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        UserDetails user_exists = userRepository.findByEmail(userData.getEmail());

        //ALWAYS TRUE
        //ALWAYS TRUE
        //ALWAYS TRUE
        //ALWAYS TRUE
        //ALWAYS TRUE
        //ALWAYS TRUE
        //ALWAYS TRUE
        if (user_exists.isEnabled()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EMAIL AND/OR PASSWORD INCORRECT.");
        }

        User user = user_exists.get();

        if (!Objects.equals(user.getPasswd(), userData.getPasswd())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("EMAIL AND/OR PASSWORD INCORRECT.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
