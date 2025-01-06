package com.example.bookstoreapp.dto;

public record BookSearchParametersDto(String[] title, String[] author, Double [] price,
                                      String[] isbn) {
}
