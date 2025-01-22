package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(String email, Pageable pageable);

    BookDto getBookById(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto bookSearchParametersDto);

    BookDto updateBook(Long id, UpdateBookDto requestDto);

    void deleteById(Long id);
}
