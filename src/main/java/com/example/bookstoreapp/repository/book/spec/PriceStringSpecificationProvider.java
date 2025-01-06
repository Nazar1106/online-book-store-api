package com.example.bookstoreapp.repository.book.spec;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.DoubleSpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceStringSpecificationProvider implements DoubleSpecificationProvider<Book> {

    public static final String PRICE = "price";

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Book> getSpecification(Double[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(PRICE).in(Arrays.stream(params).toArray());
    }
}
