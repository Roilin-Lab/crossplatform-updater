package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Platform;

import java.util.List;


public interface AppVersionRepository extends JpaRepository<AppVersion, Integer> {
  List<AppVersion> findByPlatform(Platform platform);
  AppVersion findFirstByPlatformAndIsActiveOrderByReleaseDateDesc(Platform platform, boolean isActive);
}
