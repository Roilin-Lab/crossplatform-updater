package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public interface AppVersionService {
  List<AppVersion> getAll(Platform platform);

  AppVersion getById(Integer id);
  
  AppVersion getLatesByPlatform(Platform platform);

  AppVersion getByVersionAndPlatform(String version, Platform platform);

  AppVersion create(AppVersion version);

  void deleteById(Integer id);
}
