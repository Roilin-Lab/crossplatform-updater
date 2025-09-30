package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

import java.util.List;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Integer> {
  List<AppVersion> findAllByPlatform(Platform platform);

  AppVersion findFirstByPlatformAndIsActiveTrueOrderByReleaseDateDesc(Platform platform);

  AppVersion findByVersionAndPlatform(String version, Platform platform);
}
