package io.github.roilin.crossplatform_updater.controllers;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.ApplicationRequest;
import io.github.roilin.crossplatform_updater.dto.ApplicationResponse;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.services.ApplicationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/apps")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  @GetMapping()
  public List<ApplicationResponse> getAll() {
    return applicationService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(applicationService.getById(id));
  }

  @GetMapping("/filter")
  public ResponseEntity<Object> filter(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) String developer,
      @RequestParam(required = false) ApplicationType type,
      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
      @RequestParam(value = "count", defaultValue = "10", required = false) int size,
      @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
      @RequestParam(value = "sort", defaultValue = "title", required = false) String sortProperty) {
    Pageable pageable = PageRequest.of(page, size, direction, sortProperty);
    return ResponseEntity.status(HttpStatus.OK)
        .body(applicationService.getAllByFilter(title, publisher, developer, type, pageable));
  }

  @PostMapping()
  public ResponseEntity<ApplicationResponse> createApplication(@RequestBody ApplicationRequest entity) {
    return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.create(entity));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApplicationResponse> updateApplication(@RequestBody ApplicationRequest entity,
      @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(applicationService.update(entity, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
    applicationService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("Appliaction deleted successfully.");
  }
}
