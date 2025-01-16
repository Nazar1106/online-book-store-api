package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(BookSearchParametersDto searchParameters);
}
