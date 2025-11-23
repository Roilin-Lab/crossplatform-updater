package io.github.roilin.crossplatform_updater.mapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.models.UserDevice;

public class UserDeviceMapper {
  public static UserDeviceResponse toResponseDto(UserDevice d) {
    return new UserDeviceResponse(
        d.getId(),
        d.getName(),
        d.getPlatform(),
        d.getUser().getUsername(),
        d.getInstalledApps().stream().map(AppVersionMapper::toDto).collect(Collectors.toSet()));
  }

  public static UserDevice toEntity(UserDeviceRequest req) {
    UserDevice entity = new UserDevice();
    entity.setName(req.getName());
    entity.setPlatform(req.getPlatform());
    entity.setLastSeen(LocalDateTime.now());
    return entity;
  }
}
