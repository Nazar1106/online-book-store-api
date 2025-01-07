package com.example.bookstoreapp.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public interface DoubleSpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(BigDecimal[] params);
}
