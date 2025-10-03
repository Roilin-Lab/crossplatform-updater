package io.github.roilin.crossplatform_updater.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.roilin.crossplatform_updater.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
      WebRequest request) {
    ErrorResponse response = new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest request) {
    ErrorResponse response = new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
}
