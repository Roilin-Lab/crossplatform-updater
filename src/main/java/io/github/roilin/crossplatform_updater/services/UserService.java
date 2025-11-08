package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.UserDto;
import io.github.roilin.crossplatform_updater.models.user.User;

public interface UserService {
    UserDto getUserDto(Long id);

    UserDto getUserDto(String username);

    User getUser(String username);

    List<UserDto> getUsers();

    User changePassword(String username, String newPassword);
}
