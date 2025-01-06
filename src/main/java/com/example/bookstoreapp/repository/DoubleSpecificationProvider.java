package com.example.bookstoreapp.repository;

import org.springframework.data.jpa.domain.Specification;

public interface DoubleSpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(Double[] params);
}
