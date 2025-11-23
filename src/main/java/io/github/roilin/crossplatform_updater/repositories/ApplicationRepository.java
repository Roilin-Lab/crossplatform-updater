package io.github.roilin.crossplatform_updater.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.github.roilin.crossplatform_updater.models.Application;
import java.util.List;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
  List<Application> findByDeveloper(String developer);

  List<Application> findByPublisher(String publisher);

  List<Application> findByType(ApplicationType type);
}
