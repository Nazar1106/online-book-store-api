package com.example.bookstoreapp.exception.advice;

import com.example.bookstoreapp.exception.ApiError;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.exception.OrderProcessingException;
import com.example.bookstoreapp.exception.RegistrationException;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    public static final String VALIDATION_EXCEPTION_MSG = "Validation exception";
    public static final String NO_RESOURCE_FOUND_MSG = "The resource doesn't exist";
    public static final String RESOURCE_CONFLICT = "The resource conflict";
    public static final String AUTHENTICATION_EXCEPTION = "Authentication exception";
    public static final String ORDER_PROCESSING_EXCEPTION = "Order could not be processed";
    public static final String ENTITY_NOT_FOUND_EXCEPTION = "Entity not found exception";
    public static final String AUTHORIZATION_EXCEPTION = "Authorization exception";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String collect = ex.getBindingResult().getFieldErrors().stream()
                .map((DefaultMessageSourceResolvable::getDefaultMessage))
                .collect(Collectors.joining());

        ApiError apiError = new ApiError(
                VALIDATION_EXCEPTION_MSG,
                collect,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {OrderProcessingException.class})
    public ResponseEntity<ApiError> handleProcessingException(OrderProcessingException ex) {
        ApiError apiError = new ApiError(
                ORDER_PROCESSING_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(
                VALIDATION_EXCEPTION_MSG,
                ex.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(
                VALIDATION_EXCEPTION_MSG,
                ex.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {RegistrationException.class})
    public ResponseEntity<ApiError> handleRegistrationException(RegistrationException ex) {
        ApiError apiError = new ApiError(
                RESOURCE_CONFLICT,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AuthorizationDeniedException.class})
    public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationDeniedException ex) {
        ApiError apiError = new ApiError(
                AUTHORIZATION_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(
            InternalAuthenticationServiceException ex) {
        ApiError apiError = new ApiError(
                AUTHENTICATION_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<ApiError> handleNoResourceException(NoResourceFoundException ex) {
        ApiError apiError = new ApiError(
                NO_RESOURCE_FOUND_MSG,
                ex.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(
                ENTITY_NOT_FOUND_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
