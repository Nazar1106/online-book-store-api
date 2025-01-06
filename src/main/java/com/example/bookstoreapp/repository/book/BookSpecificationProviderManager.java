package com.example.bookstoreapp.repository.book;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.DoubleSpecificationProvider;
import com.example.bookstoreapp.repository.SpecificationProviderManager;
import com.example.bookstoreapp.repository.StringSpecificationProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    public static final String CAN_T_FIND_CORRECT_SPECIFICATION_PROVIDER =
            "Can't find correct specification provider for key ";

    private final List<StringSpecificationProvider<Book>> bookStringSpecificationProviders;

    private final List<DoubleSpecificationProvider<Book>> doubleSpecificationProviders;

    @Override
    public StringSpecificationProvider<Book> getStringSpecificationProvider(String key) {
        return bookStringSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new RuntimeException(CAN_T_FIND_CORRECT_SPECIFICATION_PROVIDER + key));
    }

    @Override
    public DoubleSpecificationProvider<Book> getDoubleSpecificationProvider(String key) {
        return doubleSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new RuntimeException(CAN_T_FIND_CORRECT_SPECIFICATION_PROVIDER + key));
    }
}
