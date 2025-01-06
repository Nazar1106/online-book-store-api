package com.example.bookstoreapp.repository.book.spec;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.StringSpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleStringSpecificationProvider implements StringSpecificationProvider<Book> {

    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    public Specification<Book> getSpecification(String [] params) {
        return (root, query, criteriaBuilder)
                -> root.get(TITLE)
                .in(Arrays.stream(params)
                        .toArray());
    }
}
