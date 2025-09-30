package io.github.roilin.crossplatform_updater.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

  private final AppVersionRepository appVersionRepository;

  @Override
  public List<AppVersion> getAll(Platform platform) {
    if (platform == null) {
      return (List<AppVersion>) appVersionRepository.findAll();
    }
    return (List<AppVersion>) appVersionRepository.findAllByPlatform(platform);
  }

  @Override
  public AppVersion getById(Integer id) {
    return appVersionRepository.findById(id).orElse(null);
  }

  @Override
  public AppVersion getLatesByPlatform(Platform platform) {
    return appVersionRepository.findFirstByPlatformAndIsActiveTrueOrderByReleaseDateDesc(platform);
  }

  @Override
  public AppVersion getByVersionAndPlatform(String version, Platform platform) {
    return appVersionRepository.findByVersionAndPlatform(version, platform);
  }

  @Override
  public AppVersion create(AppVersion appVersion) {
    return appVersionRepository.save(appVersion);
  }

  @Override
  public void deleteById(Integer id) {
    appVersionRepository.deleteById(id);
  }
}
