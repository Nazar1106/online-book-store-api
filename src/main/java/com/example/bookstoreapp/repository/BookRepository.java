package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> getBookById(Long id);
}
