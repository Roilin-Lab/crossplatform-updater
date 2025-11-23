package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

import java.util.List;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long>, JpaSpecificationExecutor<AppVersion> {
  List<AppVersion> findAllByApplication(Application application);

  AppVersion findFirstByApplicationAndIsActiveTrueOrderByReleaseDateDesc(Application application);

  AppVersion findByVersionAndPlatform(String version, Platform platform);
}
