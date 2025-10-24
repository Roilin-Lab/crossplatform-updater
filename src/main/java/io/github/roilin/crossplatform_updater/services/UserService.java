package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.UserDto;

public interface UserService {
    UserDto getUser(Long id);

    UserDto getUser(String username);

    List<UserDto> getUsers();
}
