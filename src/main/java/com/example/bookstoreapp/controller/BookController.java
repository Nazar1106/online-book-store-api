package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import com.example.bookstoreapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "books", description = "Provides endpoints for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Retrieve paginated list of books",
            description = "Fetches a paginated and sorted list of all available books. "
                    + "Sorting can be applied by providing the sorting criteria "
                    + "in the request parameters. "
                    + "The sorting criteria should be specified in the format: "
                    + "'sort: field,(ASC||DESC)' "
                    + "where 'ASC' is ascending and 'DESC' is descending. "
                    + "Default sorting is by in ascending order. "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Retrieve a book by its ID",
            description = "Fetches a book from the system using its unique identifier. "
                    + "Only users with 'USER' authority can access this endpoint.")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Search books by specific criteria",
            description = "Retrieves a list of books that match the specified search criteria. "
                    + "Filtering parameters should be provided as request parameters. "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        return bookService.searchBooks(bookSearchParametersDto);
    }

    @Operation(
            summary = "Create a new book",
            description = "Adds a new book to the catalog. Only users with "
                    + "'ADMIN' authority can perform this operation. "
                    + "The request body must contain valid book details."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(
            summary = "Update book by ID",
            description = "Updates an existing book identified by the given ID. "
                    + "Only users with 'ADMIN' authority can perform this operation. "
                    + "The request body must contain valid book details."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookDto book) {
        return bookService.updateBook(id, book);
    }

    @Operation(
            summary = "Delete book by ID",
            description = "Deletes a book identified by the given ID. "
                    + "Only users with 'ADMIN' authority can perform this operation."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
