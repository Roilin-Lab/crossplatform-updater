package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.repository.CrudRepository;

import io.github.roilin.crossplatform_updater.models.AppVersion;

public interface AppVersionRepository extends CrudRepository<AppVersion, Integer> {}
