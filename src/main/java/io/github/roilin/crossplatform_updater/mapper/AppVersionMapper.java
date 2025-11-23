package io.github.roilin.crossplatform_updater.mapper;

import io.github.roilin.crossplatform_updater.dto.AppVersionDto;
import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.models.AppVersion;

public class AppVersionMapper {
  public static AppVersionDto toDto(AppVersion ver) {
    return new AppVersionDto(
        ver.getId(),
        ver.getVersion(),
        ver.getPlatform(),
        ver.getReleaseDate(),
        ApplicationMapper.toResponseDto(ver.getApplication()),
        ver.getChangeLog(),
        ver.isActive(),
        ver.getUpdateType());
  }

  public static AppVersionResponse toResponseDto(AppVersion ver) {
    return new AppVersionResponse(
        ver.getId(),
        ver.getVersion(),
        ver.getPlatform(),
        ver.getReleaseDate(),
        ApplicationMapper.toResponseDto(ver.getApplication()),
        ver.getChangeLog(),
        ver.isActive(),
        ver.getUpdateType());
  }

  public static AppVersion toEntity(AppVersionRequest req) {
    AppVersion entity = new AppVersion();
    entity.setVersion(req.getVersion());
    entity.setPlatform(req.getPlatform());
    entity.setReleaseDate(req.getReleaseDate());
    entity.setChangeLog(req.getChangeLog());
    entity.setActive(req.isActive());
    entity.setUpdateType(req.getUpdateType());
    return entity;
  }
}
