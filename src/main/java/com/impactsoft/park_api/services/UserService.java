package com.impactsoft.park_api.services;

import com.impactsoft.park_api.entities.User;
import com.impactsoft.park_api.exception.EntityNotFoundException;
import com.impactsoft.park_api.exception.UsernameUniqueViolationException;
import com.impactsoft.park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException exception) {
            throw new UsernameUniqueViolationException(String.format("Username %s already exists!", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User id = %s not found!", id))
        );
    }

    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw  new RuntimeException("New password doesn't match confirmed password!");
        }

        User user = findById(id);

        if (!user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Your password doesn't match");
        }

        user.setPassword(newPassword);

        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
