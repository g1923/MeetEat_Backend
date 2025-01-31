package com.zb.meeteat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.")
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
