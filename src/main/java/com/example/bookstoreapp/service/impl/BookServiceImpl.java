package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.exception.EntityNotFoundException;
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

    private static final String CAN_T_FIND_BOOK_MSG = "Can't find book";

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
        Optional<Book> bookById = bookRepository.findById(id);
        return bookById.map(bookMapper::bookToBookDto)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_FIND_BOOK_MSG + id));
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto book) {
        Optional<Book> bookById = bookRepository.findById(id);
        if (bookById.isPresent()) {
            bookRepository.updateBookById(id, book);
        }
        return bookMapper
                .bookToBookDto(bookById.orElseThrow(()
                        -> new EntityNotFoundException("Can't update book by id " + id)));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
