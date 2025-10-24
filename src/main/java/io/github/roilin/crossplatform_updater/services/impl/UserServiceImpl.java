package io.github.roilin.crossplatform_updater.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.UserDto;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.UserMapper;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }
}
