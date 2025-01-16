package com.example.bookstoreapp.dto.bookdto;

import java.math.BigDecimal;

public record BookSearchParametersDto(String[] title, String[] author, BigDecimal[] price,
                                      String[] isbn) {
}
