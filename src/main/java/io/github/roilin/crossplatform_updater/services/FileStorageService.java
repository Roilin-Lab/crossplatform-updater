package io.github.roilin.crossplatform_updater.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  public String storeFile(MultipartFile file) throws IOException, SecurityException;

  public Resource loadFileAsResource(String fileName) throws MalformedURLException;
}
