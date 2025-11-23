package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.ApplicationMapper;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.repositories.ApplicationRepository;
import io.github.roilin.crossplatform_updater.services.ApplicationService;
import io.github.roilin.crossplatform_updater.specifications.ApplicationSpecifications;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final ApplicationRepository applicationRepository;

  @Override
  public List<ApplicationResponse> getAll() {
    return applicationRepository.findAll().stream().map(ApplicationMapper::toResponseDto).toList();
  }

  @Override
  public List<ApplicationResponse> getAllByPublisher(String publisher) {
    return applicationRepository.findByPublisher(publisher).stream().map(ApplicationMapper::toResponseDto).toList();
  }

  @Override
  public List<ApplicationResponse> getAllByDeveloper(String developer) {
    return applicationRepository.findByDeveloper(developer).stream().map(ApplicationMapper::toResponseDto).toList();
  }

  @Override
  public List<ApplicationResponse> getAllByType(ApplicationType type) {
    return applicationRepository.findByType(type).stream().map(ApplicationMapper::toResponseDto).toList();
  }

  @Override
  public ApplicationResponse getById(Long id) {
    return ApplicationMapper.toResponseDto(applicationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id.toString())));
  }

  @Override
  public Page<ApplicationResponse> getAllByFilter(String title, String publisher, String developer, ApplicationType type, Pageable pageable) {
    return applicationRepository.findAll(ApplicationSpecifications.filter(title, publisher, developer, type), pageable).map(ApplicationMapper::toResponseDto);
  }

  @Override
  public ApplicationResponse create(ApplicationRequest application) {
    return ApplicationMapper.toResponseDto(applicationRepository.save(ApplicationMapper.toEntity(application)));
  }

  @Override
  public ApplicationResponse update(ApplicationRequest application, Long id) {
    Application updatedApp = applicationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id.toString()));
    updatedApp.setTitle(application.title());
    updatedApp.setType(application.type());
    updatedApp.setSupportedPlatform(application.supportedPlatform());
    updatedApp.setDeveloper(application.developer());
    updatedApp.setPublisher(application.publisher());
    updatedApp.setLastUpdate(LocalDateTime.now());
    return ApplicationMapper.toResponseDto(applicationRepository.save(updatedApp));
  }

  @Override
  public void deleteById(Long id) {
    applicationRepository.deleteById(id);
  }
}
