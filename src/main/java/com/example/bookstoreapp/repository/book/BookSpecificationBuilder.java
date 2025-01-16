package com.example.bookstoreapp.repository.book;

import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.SpecificationBuilder;
import com.example.bookstoreapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String ISBN = "isbn";
    public static final String AUTHOR = "author";
    public static final int ZERO = 0;

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.author() != null && searchParameters.author().length > ZERO) {
            spec = spec.and(bookSpecificationProviderManager.getStringSpecificationProvider(AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > ZERO) {
            spec = spec.and(bookSpecificationProviderManager.getStringSpecificationProvider(ISBN)
                    .getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > ZERO) {
            spec = spec.and(bookSpecificationProviderManager.getStringSpecificationProvider(TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.price() != null) {
            spec = spec.and(bookSpecificationProviderManager.getDoubleSpecificationProvider(PRICE)
                    .getSpecification(searchParameters.price()));
        }
        return spec;
    }
}
