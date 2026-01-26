package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.services.impl.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("report/download")
  public ResponseEntity<Resource> downloadOReport() {

    Resource resource = reportService.buildReport();

    return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=outdated_report.xlsx")
        .body(resource);
  }
}
