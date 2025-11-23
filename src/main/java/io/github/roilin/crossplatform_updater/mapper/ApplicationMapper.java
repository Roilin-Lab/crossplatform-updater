package io.github.roilin.crossplatform_updater.mapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import io.github.roilin.crossplatform_updater.dto.ApplicationDto;
import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.models.Application;

public class ApplicationMapper {
  public static ApplicationDto toDto(Application app) {
    return new ApplicationDto(
        app.getId(),
        app.getTitle(),
        app.getType(),
        app.getSupportedPlatform(),
        app.getDeveloper(),
        app.getPublisher(),
        app.getLastUpdate(),
        app.getReleaseDate());
  }

  public static ApplicationResponse toResponseDto(Application app) {
    return new ApplicationResponse(
        app.getId(),
        app.getTitle(),
        app.getType(),
        app.getSupportedPlatform(),
        app.getDeveloper(),
        app.getPublisher(),
        app.getLastUpdate(),
        app.getReleaseDate(),
        app.getVersions().stream().map(AppVersionMapper::toDto).collect(Collectors.toSet()));
  }

  public static Application toEntity(ApplicationRequest req) {
    Application entity = new Application();
    entity.setTitle(req.title());
    entity.setType(req.type());
    entity.setSupportedPlatform(req.supportedPlatform());
    entity.setDeveloper(req.developer());
    entity.setPublisher(req.publisher());
    entity.setLastUpdate(LocalDateTime.now());
    entity.setReleaseDate(LocalDateTime.now());
    return entity;
  }
}
