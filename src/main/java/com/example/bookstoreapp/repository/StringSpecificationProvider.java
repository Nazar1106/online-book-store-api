package com.example.bookstoreapp.repository;

import org.springframework.data.jpa.domain.Specification;

public interface StringSpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(String[] params);
}
