package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.UpdateBookDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.repository.book.BookSpecificationBuilder;
import com.example.bookstoreapp.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String CANT_UPDATE_BOOK_BY_ID_MSG = "Can't update book by id ";
    private static final String CAN_T_DELETE_BOOK_BY_ID_MSG = "Can't delete book by id ";
    private static final String CAN_T_FIND_BOOK_MSG = "Can't find book";

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder specificationBuilder;

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
    public List<BookDto> searchBooks(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification = specificationBuilder
                .build(bookSearchParametersDto);
        List<Book> bookList = bookRepository.findAll(bookSpecification);
        return bookMapper.listBookToListBookDto(bookList);
    }

    @Override
    public BookDto updateBook(Long id, UpdateBookDto book) {
        bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(CANT_UPDATE_BOOK_BY_ID_MSG + id));
        Book updatedBook = bookMapper.updateBookToBook(book);
        bookRepository.save(updatedBook);
        return bookMapper.bookToBookDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(CAN_T_DELETE_BOOK_BY_ID_MSG + id);
        }
        bookRepository.deleteById(id);
    }
}
