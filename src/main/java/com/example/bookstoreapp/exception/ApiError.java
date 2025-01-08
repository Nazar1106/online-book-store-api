package com.example.bookstoreapp.exception;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

public record ApiError(String message, HttpStatus httpStatus, ZonedDateTime dateTime) {
}
