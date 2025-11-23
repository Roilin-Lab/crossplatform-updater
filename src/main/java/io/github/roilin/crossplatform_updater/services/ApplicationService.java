package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;

public interface ApplicationService {
  ApplicationResponse getById(Long id);
  
  List<ApplicationResponse> getAll();

  List<ApplicationResponse> getAllByPublisher(String publisher);
  
  List<ApplicationResponse> getAllByDeveloper(String developer);

  List<ApplicationResponse> getAllByType(ApplicationType type);
  
  Page<ApplicationResponse> getAllByFilter(String title, String publisher, String developer, ApplicationType type, Pageable pageable);

  ApplicationResponse create(ApplicationRequest application);

  ApplicationResponse update(ApplicationRequest application, Long id);

  void deleteById(Long id);

}
