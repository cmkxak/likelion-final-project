package com.likelion.mutsasns.domain.dto.error;

import com.likelion.mutsasns.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
}
