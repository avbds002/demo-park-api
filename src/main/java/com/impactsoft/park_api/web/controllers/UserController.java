package com.impactsoft.park_api.web.controllers;

import com.impactsoft.park_api.entities.User;
import com.impactsoft.park_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody  User user) {
        User user1 = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //api/v1/users/1
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user1 = userService.findById(id);
        return ResponseEntity.ok(user1);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody User user) {
        User user1 = userService.updatePassword(id, user.getPassword());
        return ResponseEntity.ok(user1);
    }

    

}
