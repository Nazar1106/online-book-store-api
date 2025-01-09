package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.UpdateBookDto;
import com.example.bookstoreapp.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search")
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        return bookService.searchBooks(bookSearchParametersDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookDto book) {
        return bookService.updateBook(id, book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
