package io.github.roilin.crossplatform_updater.mapper;

import java.util.stream.Collectors;

import io.github.roilin.crossplatform_updater.dto.UserDto;
import io.github.roilin.crossplatform_updater.models.user.Permission;
import io.github.roilin.crossplatform_updater.models.user.User;

public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRole().getAuthority(),
            user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet())
        );
    }
}
