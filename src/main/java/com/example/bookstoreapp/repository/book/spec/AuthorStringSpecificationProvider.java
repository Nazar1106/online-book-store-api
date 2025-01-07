package com.example.bookstoreapp.repository.book.spec;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.StringSpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorStringSpecificationProvider implements StringSpecificationProvider<Book> {

    @Override
    public String getKey() {
        return SpecConstraint.AUTHOR.getSpecC();
    }

    public Specification<Book> getSpecification(String [] params) {
        return (root, query, criteriaBuilder)
                -> root.get(SpecConstraint.AUTHOR.getSpecC())
                .in(Arrays.stream(params)
                        .toArray());
    }
}
