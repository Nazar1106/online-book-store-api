package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.repository.book.BookSpecificationBuilder;
import com.example.bookstoreapp.service.BookService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String CANT_UPDATE_BOOK_BY_ID_MSG = "Can't update book by id ";

    private static final String CANT_DELETE_BOOK_BY_ID_MSG = "Can't delete book by id ";

    private static final String CANT_FIND_BOOK_MSG = "Can't find book by id ";

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookDto> findAll(String email, Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CANT_FIND_BOOK_MSG + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> searchBooks(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification = specificationBuilder
                .build(bookSearchParametersDto);
        List<Book> bookList = bookRepository.findAll(bookSpecification);
        return bookMapper.toDtos(bookList);
    }

    @Override
    public BookDto updateBook(Long id, UpdateBookDto book) {
        bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(CANT_UPDATE_BOOK_BY_ID_MSG + id));
        Book updatedBook = bookMapper.updateBookFromDto(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(CANT_DELETE_BOOK_BY_ID_MSG + id);
        }
        bookRepository.deleteById(id);
    }
}
