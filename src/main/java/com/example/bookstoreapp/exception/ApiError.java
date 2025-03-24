package com.example.bookstoreapp.exception;

import java.time.ZonedDateTime;

public record ApiError(String errorType,String message, ZonedDateTime dateTime) {
}
