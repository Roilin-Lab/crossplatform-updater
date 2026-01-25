package io.github.roilin.crossplatform_updater.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.ApplicationMapper;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.repositories.ApplicationRepository;
import io.github.roilin.crossplatform_updater.services.ApplicationService;
import io.github.roilin.crossplatform_updater.services.FileStorageService;
import io.github.roilin.crossplatform_updater.specifications.ApplicationSpecifications;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

  private final ApplicationRepository applicationRepository;
  private final FileStorageService fileStorageService;

  @Override
  public Application findById(Long id) {
    logger.debug("Fetching application by ID: {}", id);
    return applicationRepository.findById(id)
        .orElseThrow(() -> {
          logger.warn("Application not found with ID: {}", id);
          return new ResourceNotFoundException("Application", "id", id.toString());
        });
  }

  @Override
  public List<ApplicationResponse> getAll() {
    logger.debug("Retrieving all applications");
    List<ApplicationResponse> applications = applicationRepository.findAll()
        .stream()
        .map(ApplicationMapper::toResponseDto)
        .collect(Collectors.toList());
    logger.debug("Found {} applications", applications.size());
    return applications;
  }

  @Override
  public List<ApplicationResponse> getAllByPublisher(String publisher) {
    logger.debug("Fetching applications by publisher: {}", publisher);
    List<ApplicationResponse> applications = applicationRepository.findByPublisher(publisher)
        .stream()
        .map(ApplicationMapper::toResponseDto)
        .collect(Collectors.toList());
    logger.debug("Found {} applications for publisher: {}", applications.size(), publisher);
    return applications;
  }

  @Override
  public List<ApplicationResponse> getAllByDeveloper(String developer) {
    logger.debug("Fetching applications by developer: {}", developer);
    List<ApplicationResponse> applications = applicationRepository.findByDeveloper(developer)
        .stream()
        .map(ApplicationMapper::toResponseDto)
        .collect(Collectors.toList());
    logger.debug("Found {} applications for developer: {}", applications.size(), developer);
    return applications;
  }

  @Override
  public List<ApplicationResponse> getAllByType(ApplicationType type) {
    logger.debug("Fetching applications by type: {}", type);
    List<ApplicationResponse> applications = applicationRepository.findByType(type)
        .stream()
        .map(ApplicationMapper::toResponseDto)
        .collect(Collectors.toList());
    logger.debug("Found {} applications of type: {}", applications.size(), type);
    return applications;
  }

  @Override
  public ApplicationResponse getById(Long id) {
    logger.debug("Requesting application details for ID: {}", id);
    ApplicationResponse response = ApplicationMapper.toResponseDto(findById(id));
    logger.debug("Successfully retrieved application ID: {}", id);
    return response;
  }

  @Override
  public Page<ApplicationResponse> getAllByFilter(String title, String publisher, String developer,
      ApplicationType type, Pageable pageable) {
    logger.debug("Filtering applications: title='{}', publisher='{}', developer='{}', type='{}', page={}/{}",
        title, publisher, developer, type, pageable.getPageNumber(), pageable.getPageSize());

    Page<ApplicationResponse> page = applicationRepository.findAll(
        ApplicationSpecifications.filter(title, publisher, developer, type),
        pageable).map(ApplicationMapper::toResponseDto);

    logger.debug("Filter results: {} items on page {}/{}",
        page.getContent().size(), page.getNumber(), page.getTotalPages());
    return page;
  }

  @Override
  public boolean isPlatformSupported(Long id, Platform platform) {
    logger.debug("Checking platform support: application ID={}, platform={}", id, platform);
    Application app = findById(id);
    boolean isSupported = app.getSupportedPlatform().contains(platform);
    logger.debug("Platform {} support for application ID={}: {}", platform, id, isSupported);
    return isSupported;
  }

  @Override
  public ApplicationResponse create(ApplicationRequest application) {
    logger.info("Creating new application: title='{}', type={}", application.title(), application.type());
    Application entity = ApplicationMapper.toEntity(application);
    Application saved = applicationRepository.save(entity);
    logger.info("Created application ID: {} (title='{}')", saved.getId(), saved.getTitle());
    return ApplicationMapper.toResponseDto(saved);
  }

  @Override
  public ApplicationResponse update(ApplicationRequest application, Long id) {
    logger.info("Updating application ID: {} with title='{}'", id, application.title());

    Application updatedApp = applicationRepository.findById(id)
        .orElseThrow(() -> {
          logger.warn("Update failed: application not found with ID: {}", id);
          return new ResourceNotFoundException("Application", "id", id.toString());
        });

    if (!updatedApp.getTitle().equals(application.title())) {
      logger.info("Title changed for application ID {}: '{}' -> '{}'",
          id, updatedApp.getTitle(), application.title());
    }

    updatedApp.setTitle(application.title());
    updatedApp.setType(application.type());
    updatedApp.setSupportedPlatform(application.supportedPlatform());
    updatedApp.setDeveloper(application.developer());
    updatedApp.setPublisher(application.publisher());
    updatedApp.setLastUpdate(LocalDateTime.now());

    Application updated = applicationRepository.save(updatedApp);
    logger.info("Successfully updated application ID: {}", updated.getId());
    return ApplicationMapper.toResponseDto(updated);
  }

  @Override
  public void deleteById(Long id) {
    logger.info("Deleting application ID: {}", id);
    applicationRepository.deleteById(id);
    logger.info("Successfully deleted application ID: {}", id);
  }

  @Override
  public ApplicationResponse updateIcon(Long id, MultipartFile icon) throws SecurityException, IOException {
    logger.info("Updating icon for application ID: {}", id);
    Application app = findById(id);

    String fileName = fileStorageService.storeFile(icon);
    logger.debug("Stored icon file: {}", fileName);

    app.setIcon(fileName);
    Application updated = applicationRepository.save(app);
    logger.info("Updated icon for application ID: {} (new file: {})", id, fileName);
    return ApplicationMapper.toResponseDto(updated);
  }
}
