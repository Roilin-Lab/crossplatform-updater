package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.roilin.crossplatform_updater.models.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
