package com.example.bookstoreapp.service;

import com.example.bookstoreapp.entity.Book;
import java.util.List;

public interface BookService {

    Book save(Book book);

    List findAll();
}
