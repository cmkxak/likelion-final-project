package com.likelion.mutsasns.exception;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.error.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<?> exceptionHandler(AppException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error("ERROR", errorResponse));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> exceptionHandler(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(extractErrorMessage(e));
    }

    private List<ErrorResponse> extractErrorMessage(MethodArgumentNotValidException e){
        return e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(ErrorResponse::new)
                .collect(Collectors.toList());
    }
}
