package com.example.bookstoreapp.exception.advice;

import com.example.bookstoreapp.exception.ApiError;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.exception.RegistrationException;
import java.time.ZonedDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    public static final String VALIDATION_EXCEPTION_MSG = "Validation exception";
    public static final String NO_RESOURCE_FOUND_MSG = "The resource doesn't exist";
    public static final String RESOURCE_CONFLICT = "The resource conflict";

    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleValidationException() {
        ApiError apiError = new ApiError(
                VALIDATION_EXCEPTION_MSG,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleNoResourceException() {
        ApiError apiError = new ApiError(
                NO_RESOURCE_FOUND_MSG,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RegistrationException.class})
    public ResponseEntity<ApiError> handleRegistrationException() {
        ApiError apiError = new ApiError(RESOURCE_CONFLICT,
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
