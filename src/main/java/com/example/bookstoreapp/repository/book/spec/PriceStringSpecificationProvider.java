package com.example.bookstoreapp.repository.book.spec;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.DoubleSpecificationProvider;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceStringSpecificationProvider implements DoubleSpecificationProvider<Book> {

    public static final String AMOUNT_OF_PARAMETERS_MSG = "Wrong amount of parameters";
    public static final int ZERO = 0;
    public static final int ONE = 1;

    @Override
    public String getKey() {
        return SpecConstraint.PRICE.getSpecC();
    }

    @Override
    public Specification<Book> getSpecification(BigDecimal[] params) {
        return (root, query, criteriaBuilder) -> {
            if (params.length == 2) {
                return criteriaBuilder.between(root.get(SpecConstraint.PRICE.getSpecC()),
                        params[ZERO], params[ONE]);
            }
            throw new IllegalArgumentException(AMOUNT_OF_PARAMETERS_MSG);
        };
    }
}
