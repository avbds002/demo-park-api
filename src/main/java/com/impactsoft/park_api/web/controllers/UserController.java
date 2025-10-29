package com.impactsoft.park_api.web.controllers;

import com.impactsoft.park_api.entities.User;
import com.impactsoft.park_api.services.UserService;
import com.impactsoft.park_api.web.dto.UserDTO;
import com.impactsoft.park_api.web.dto.UserPasswordDTO;
import com.impactsoft.park_api.web.dto.UserResponseDTO;
import com.impactsoft.park_api.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserDTO userDTO) {
        User user = userService.save(UserMapper.toUser(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(user));
    }

    //api/v1/users/1
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDTO userDTO) {
        User user = userService.updatePassword(id, userDTO.getCurrentPassword(), userDTO.getNewPassword(), userDTO.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.toListDTO(users));
    }

}
