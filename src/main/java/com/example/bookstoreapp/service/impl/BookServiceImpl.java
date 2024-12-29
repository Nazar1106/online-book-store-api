package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.BookRepository;
import com.example.bookstoreapp.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toBook(requestDto);
        bookRepository.save(book);
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> bookList = bookRepository.findAll();
        return bookMapper.listBookToListBookDto(bookList);
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> bookById = bookRepository.getBookById(id);
        return bookById.map(bookMapper::bookToBookDto).orElse(null);
    }
}
