package com.example.bookstoreapp.repository.book.spec;

import lombok.Getter;

@Getter
public enum SpecConstraint {

    AUTHOR("author"), TITLE("title"), PRICE("price"), ISBN("isbn");

    private final String specC;

    SpecConstraint(String specC) {
        this.specC = specC;
    }
}
