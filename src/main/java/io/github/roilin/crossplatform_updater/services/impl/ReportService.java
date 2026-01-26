package io.github.roilin.crossplatform_updater.services.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final UserDeviceRepository userDeviceRepository;

  @Value("${report.template-location}")
  private Resource templateLocation;

  private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

  public ByteArrayResource buildReport() {
    Map<String, Object> data = new HashMap<>();
    
    List<UserDevice> device = userDeviceRepository.findAll();

    data.put("device", device);

    try (InputStream is = templateLocation.getInputStream()) {
      byte[] reportBytes = JxlsPoiTemplateFillerBuilder.newInstance().withTemplate(is).buildAndFill(data);
      logger.info("Successfully generated report");
      return new ByteArrayResource(reportBytes);
    } catch (Exception e) {
      throw new RuntimeException("Error while creating the report", e);
    }
  }
}
