package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.entity.Book;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
