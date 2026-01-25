package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.AppVersionMapper;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.github.roilin.crossplatform_updater.services.ApplicationService;
import io.github.roilin.crossplatform_updater.specifications.AppVersionSpecifications;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

  private static final Logger logger = LoggerFactory.getLogger(AppVersionServiceImpl.class);

  private final AppVersionRepository appVersionRepository;
  private final ApplicationService applicationService;

  @Override
  public List<AppVersionResponse> getAll(Long applicationId) {
    logger.debug("Fetching all versions for application ID: {}", applicationId);
    Application app = applicationService.findById(applicationId);
    List<AppVersionResponse> responses = appVersionRepository.findAllByApplication(app)
        .stream().map(AppVersionMapper::toResponseDto)
        .collect(Collectors.toList());
    logger.debug("Found {} versions for application ID: {}", responses.size(), applicationId);
    return responses;
  }

  @Override
  public AppVersionResponse getById(Long id) {
    logger.debug("Requesting app version by ID: {}", id);
    AppVersion version = appVersionRepository.findById(id)
        .orElseThrow(() -> {
          logger.warn("App version not found with ID: {}", id);
          return new ResourceNotFoundException("App version", "id", id.toString());
        });
    logger.debug("Successfully retrieved app version ID: {}", id);
    return AppVersionMapper.toResponseDto(version);
  }

  @Override
  public AppVersion findLatest(Long applicationId, Platform platform) {
    logger.debug("Searching latest version for application ID: {}, platform: {}", applicationId, platform);
    Application app = applicationService.findById(applicationId);
    AppVersion latest = appVersionRepository.findFirstByApplicationAndPlatformAndIsActiveTrueOrderByReleaseDateDesc(app,
        platform);
    if (latest == null) {
      logger.warn("No active version found for application ID: {}, platform: {}", applicationId, platform);
    } else {
      logger.debug("Found latest version ID: {} ({}) for application ID: {}",
          latest.getId(), latest.getVersion(), applicationId);
    }
    return latest;
  }

  @Override
  public AppVersionResponse getLatest(Long applicationId, Platform platform) {
    AppVersion version = findLatest(applicationId, platform);
    if (version == null) {
      logger.error("Critical: Attempted to map null latest version for app ID: {}, platform: {}", applicationId,
          platform);
      throw new IllegalStateException("No active version available");
    }
    return AppVersionMapper.toResponseDto(version);
  }

  @Override
  public AppVersionResponse getByVersionAndPlatform(String version, Platform platform) {
    logger.debug("Searching version: {}, platform: {}", version, platform);
    AppVersion appVersion = appVersionRepository.findByVersionAndPlatform(version, platform);
    if (appVersion == null) {
      logger.warn("Version {} for platform {} not found in repository", version, platform);
      throw new ResourceNotFoundException("App version", "version+platform", version + "_" + platform);
    }
    logger.debug("Successfully retrieved version {} for platform {}", version, platform);
    return AppVersionMapper.toResponseDto(appVersion);
  }

  @Override
  public AppVersionResponse create(AppVersionRequest appVersion) {
    logger.info("Creating new app version: {} (platform: {})",
        appVersion.getVersion(), appVersion.getPlatform());

    AppVersion entity = AppVersionMapper.toEntity(appVersion);
    AppVersion saved = appVersionRepository.save(entity);

    logger.info("Created app version ID: {} ({}) for application ID: {}",
        saved.getId(), saved.getVersion(), saved.getApplication().getId());
    return AppVersionMapper.toResponseDto(saved);
  }

  @Override
  public AppVersionResponse update(AppVersionRequest version, Long id) {
    logger.info("Updating app version ID: {} with new data: v{}", id, version.getVersion());

    AppVersion updatedVersion = appVersionRepository.findById(id)
        .orElseThrow(() -> {
          logger.warn("Update failed: app version not found with ID: {}", id);
          return new ResourceNotFoundException("App version", "id", id.toString());
        });

    updatedVersion.setVersion(version.getVersion());
    updatedVersion.setChangeLog(version.getChangeLog());
    updatedVersion.setReleaseDate(version.getReleaseDate());
    updatedVersion.setUpdateType(version.getUpdateType());
    updatedVersion.setPlatform(version.getPlatform());
    updatedVersion.setActive(version.isActive());

    AppVersion updated = appVersionRepository.save(updatedVersion);

    logger.info("Successfully updated app version ID: {}", updated.getId());
    return AppVersionMapper.toResponseDto(updated);
  }

  @Override
  public Page<AppVersionResponse> getByRangeDate(LocalDateTime max, LocalDateTime min, Pageable pageable) {
    logger.debug("Fetching versions in date range: {} to {} (page: {}, size: {})",
        min, max, pageable.getPageNumber(), pageable.getPageSize());

    Page<AppVersionResponse> page = appVersionRepository.findAll(
        AppVersionSpecifications.rangeDate(max, min), pageable).map(AppVersionMapper::toResponseDto);

    logger.debug("Found {} versions in date range (total pages: {})",
        page.getContent().size(), page.getTotalPages());
    return page;
  }

  @Override
  public void deleteById(Long id) {
    logger.info("Initiating deletion of app version ID: {}", id);

    if (!appVersionRepository.existsById(id)) {
      logger.warn("Deletion failed: app version not found with ID: {}", id);
      throw new ResourceNotFoundException("App version", "id", id.toString());
    }

    appVersionRepository.deleteById(id);
    logger.info("Successfully deleted app version ID: {}", id);
  }
}
