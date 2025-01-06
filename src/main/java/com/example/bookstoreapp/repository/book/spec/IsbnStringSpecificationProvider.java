package com.example.bookstoreapp.repository.book.spec;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.StringSpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnStringSpecificationProvider implements StringSpecificationProvider<Book> {

    public static final String ISBN = "isbn";

    public String getKey() {
        return ISBN;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(ISBN)
                .in(Arrays.stream(params)
                        .toArray());
    }
}
