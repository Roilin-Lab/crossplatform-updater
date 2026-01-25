package io.github.roilin.crossplatform_updater.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.UserDto;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.UserMapper;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDto getUserDto(Long id) {
        logger.debug("Fetching user DTO by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User", "id", id.toString());
                });
        logger.debug("Successfully retrieved user DTO for ID: {}", id);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserDto(String username) {
        logger.debug("Fetching user DTO by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found with username: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });
        logger.debug("Successfully retrieved user DTO for username: {}", username);
        return UserMapper.toDto(user);
    }

    @Override
    public User getUser(String username) {
        logger.debug("Fetching user entity by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User entity not found with username: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });
        logger.debug("Successfully retrieved user entity for username: {}", username);
        return user;
    }

    @Override
    public List<UserDto> getUsers() {
        logger.debug("Retrieving all users");
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        logger.debug("Found {} users", users.size());
        return users;
    }

    @Override
    public User changePassword(String username, String newPassword) {
        logger.info("Initiating password change for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Password change failed: user not found with username: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });

        user.setPassword(newPassword);
        User updatedUser = userRepository.save(user);

        logger.info("Successfully changed password for user: {}", username);
        return updatedUser;
    }
}
