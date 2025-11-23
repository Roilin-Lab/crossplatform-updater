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
import io.github.roilin.crossplatform_updater.mapper.AppVersionMapper;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import io.github.roilin.crossplatform_updater.repositories.ApplicationRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.github.roilin.crossplatform_updater.specifications.AppVersionSpecifications;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

  private final AppVersionRepository appVersionRepository;
  private final ApplicationRepository applicationRepository;

  @Override
  public List<AppVersionResponse> getAll(Long applicationId) {
    Application app = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId.toString()));
    return appVersionRepository.findAllByApplication(app)
        .stream().map(AppVersionMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public AppVersionResponse getById(Long id) {
    return AppVersionMapper.toResponseDto(appVersionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("App version", "id", id.toString())));
  }

  @Override
  public AppVersionResponse getLatest(Long applicationId) {
    Application app = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId.toString()));
    return AppVersionMapper
        .toResponseDto(appVersionRepository.findFirstByApplicationAndIsActiveTrueOrderByReleaseDateDesc(app));
  }

  @Override
  public AppVersionResponse getByVersionAndPlatform(String version, Platform platform) {
    return AppVersionMapper.toResponseDto(appVersionRepository.findByVersionAndPlatform(version, platform));
  }

  @Override
  public AppVersionResponse create(AppVersionRequest appVersion) {
    return AppVersionMapper.toResponseDto(appVersionRepository.save(AppVersionMapper.toEntity(appVersion)));
  }

  @Override
  public AppVersionResponse update(AppVersionRequest version, Long id) {
    AppVersion updatedVersion = appVersionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("App version", "id", id.toString()));
    updatedVersion.setVersion(version.getVersion());
    updatedVersion.setChangeLog(version.getChangeLog());
    updatedVersion.setReleaseDate(version.getReleaseDate());
    updatedVersion.setUpdateType(version.getUpdateType());
    updatedVersion.setPlatform(version.getPlatform());
    updatedVersion.setActive(version.isActive());
    return AppVersionMapper.toResponseDto(appVersionRepository.save(updatedVersion));
  }

  @Override
  public Page<AppVersionResponse> getByRangeDate(LocalDateTime max, LocalDateTime min, Pageable pageable) {
    return appVersionRepository.findAll(AppVersionSpecifications.rangeDate(max, min), pageable)
        .map(AppVersionMapper::toResponseDto);
  }

  @Override
  public void deleteById(Long id) {
    appVersionRepository.deleteById(id);
  }
}
