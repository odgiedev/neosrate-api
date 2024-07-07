package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.user.UserDto;
import com.neosrate.neosrate.data.dto.user.UserSignInDto;
import com.neosrate.neosrate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userData) {
        return userService.createUser(userData);
    }

    @GetMapping("/get/{id}/{ownerId}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/get/recent")
    public ResponseEntity<?> getRecentUser() {
        return userService.getRecentUser();
    }

//    @PutMapping("/update/{id}/{ownerId}")
//    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDto userUpdateData) {
//        return userService.updateUser(id, userUpdateData);
//    }

    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer ownerId) {
        return userService.deleteUser(ownerId);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userData) {
        return userService.signIn(userData);
    }
}
