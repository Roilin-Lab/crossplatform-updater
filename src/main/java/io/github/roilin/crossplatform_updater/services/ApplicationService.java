package io.github.roilin.crossplatform_updater.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public interface ApplicationService {
  Application findById(Long id);

  ApplicationResponse getById(Long id);
  
  List<ApplicationResponse> getAll();

  List<ApplicationResponse> getAllByPublisher(String publisher);
  
  List<ApplicationResponse> getAllByDeveloper(String developer);

  List<ApplicationResponse> getAllByType(ApplicationType type);
  
  Page<ApplicationResponse> getAllByFilter(String title, String publisher, String developer, ApplicationType type, Pageable pageable);

  boolean isPlatformSupported(Long id, Platform platform);

  ApplicationResponse create(ApplicationRequest application);

  ApplicationResponse update(ApplicationRequest application, Long id);

  void deleteById(Long id);

  ApplicationResponse updateIcon(Long id, MultipartFile icon) throws SecurityException, IOException;
}
