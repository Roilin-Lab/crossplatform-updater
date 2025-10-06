package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.github.roilin.crossplatform_updater.specifications.AppVersionSpecifications;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

  private final AppVersionRepository appVersionRepository;

  @Override
  public List<AppVersionResponse> getAll(Platform platform) {
    return appVersionRepository.findAll(AppVersionSpecifications.filterByPlatform(platform))
        .stream().map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public AppVersionResponse getById(Integer id) {
    return toDto(appVersionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("App version", "id", id.toString())));
  }

  @Override
  public AppVersionResponse getLatesByPlatform(Platform platform) {
    return toDto(appVersionRepository.findFirstByPlatformAndIsActiveTrueOrderByReleaseDateDesc(platform));
  }

  @Override
  public AppVersionResponse getByVersionAndPlatform(String version, Platform platform) {
    return toDto(appVersionRepository.findByVersionAndPlatform(version, platform));
  }

  @Override
  public AppVersionResponse create(AppVersionRequest appVersion) {
    return toDto(appVersionRepository.save(toEntity(appVersion)));
  }

  @Override
  public AppVersionResponse update(AppVersionRequest version, Integer id) {
    AppVersion updatedVersion = appVersionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("App version", "id", id.toString()));
    updatedVersion.setVersion(version.getVersion());
    updatedVersion.setChangeLog(version.getChangeLog());
    updatedVersion.setReleaseDate(version.getReleaseDate());
    updatedVersion.setUpdateType(version.getUpdateType());
    updatedVersion.setPlatform(version.getPlatform());
    updatedVersion.setActive(version.isActive());
    return toDto(appVersionRepository.save(updatedVersion));
  }

  @Override
  public Page<AppVersionResponse> getByRangeDate(LocalDateTime max, LocalDateTime min, Pageable pageable) {
    return appVersionRepository.findAll(AppVersionSpecifications.rangeDate(max, min), pageable).map(this::toDto);
  }

  @Override
  public void deleteById(Integer id) {
    appVersionRepository.deleteById(id);
  }

  private AppVersionResponse toDto(AppVersion version) {
    return new AppVersionResponse(
        version.getId(),
        version.getVersion(),
        version.getPlatform(),
        version.getReleaseDate(),
        version.getChangeLog(),
        version.isActive(),
        version.getUpdateType());
  }

  private AppVersion toEntity(AppVersionRequest dto) {
    AppVersion entity = new AppVersion();
    entity.setVersion(dto.getVersion());
    entity.setChangeLog(dto.getChangeLog());
    entity.setReleaseDate(dto.getReleaseDate());
    entity.setUpdateType(dto.getUpdateType());
    entity.setPlatform(dto.getPlatform());
    entity.setActive(dto.isActive());
    return entity;
  }
}
