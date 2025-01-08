package com.example.bookstoreapp.advice;

import com.example.bookstoreapp.exception.ApiError;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(ex.getMessage(),
                NOT_FOUND,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }
}
