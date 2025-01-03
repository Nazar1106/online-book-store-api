package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.BookRepository;
import com.example.bookstoreapp.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
