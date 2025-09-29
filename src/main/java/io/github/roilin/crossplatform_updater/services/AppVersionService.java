package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public interface AppVersionService {
  List<AppVersion> findAll();

  AppVersion findById(Integer id);
  
  AppVersion findLatesByPlatform(Platform platform);

  AppVersion save(AppVersion version);

  void deleteById(Integer id);
}
