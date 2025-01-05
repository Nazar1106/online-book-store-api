package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);
}
