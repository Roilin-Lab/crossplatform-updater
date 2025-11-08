package io.github.roilin.crossplatform_updater.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserBadRequestException extends RuntimeException {

  public UserBadRequestException(String message) {
    super(message);
  }
}
