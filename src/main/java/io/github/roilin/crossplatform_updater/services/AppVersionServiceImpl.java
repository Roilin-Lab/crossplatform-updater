package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

  private final AppVersionRepository appVersionRepository;

  @Override
  public List<AppVersion> findAll() {
    return (List<AppVersion>) appVersionRepository.findAll();
  }

  @Override
  public AppVersion findById(Integer id) {
    return appVersionRepository.findById(id).orElse(null);
  }

  @Override
  public AppVersion findLatesByPlatform(Platform platform) {
    return appVersionRepository.findFirstByPlatformAndIsActiveOrderByReleaseDateDesc(platform, true);
  }

  @Override
  public AppVersion save(AppVersion appVersion) {
    return appVersionRepository.save(appVersion);
  }

  @Override
  public void deleteById(Integer id) {
    appVersionRepository.deleteById(id);
  }
}
