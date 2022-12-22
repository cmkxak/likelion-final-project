package com.likelion.mutsasns.exception;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<?> exceptionHandler(AppException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error("ERROR", errorResponse));
    }
}
