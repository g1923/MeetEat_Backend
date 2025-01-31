package com.zb.meeteat.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler({CustomException.class})
  public ResponseEntity<CustomException.CustomExceptionResponse> handleCustomException(
      HttpServletRequest request, final CustomException e) {
    return ResponseEntity
        .status(e.getStatus())
        .body(CustomException.CustomExceptionResponse.builder()
            .message(e.getMessage())
            .code(e.getErrorCode().name())
            .status(e.getStatus())
            .build());
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class ExceptionResponse {
    private String message;
    private ErrorCode errorCode;
  }
}
