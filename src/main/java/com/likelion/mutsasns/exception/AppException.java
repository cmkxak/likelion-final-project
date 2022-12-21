package com.likelion.mutsasns.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message + "는 이미 있습니다.";
    }
}
