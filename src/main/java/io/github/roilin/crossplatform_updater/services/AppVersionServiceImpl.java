package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Platform;
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
    return appVersionRepository.findById(id).orElseThrow();
  }

  @Override
  public AppVersion findLatesByPlatform(Platform platform) {
    List<AppVersion> appVersions = appVersionRepository.findByPlatform(platform);
    appVersions.sort(null);
    return appVersions.get(0);
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
