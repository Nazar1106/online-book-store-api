package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import com.example.bookstoreapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.awt.print.Book;
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

    @Operation(summary = "Get books by pages",
            description = "The method returns all available books on specific pages "
                    + "and can also sort the data "
                    + "based on parameters in the request ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the books",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Request is invalid",
                    content = @Content)})
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by id",
            description = "The method returns specific book by unique ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "The book is found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Request is invalid", content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The book was not found by this ID",
                    content = @Content),
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Get books by specific parameters",
            description = "The method returns books based on specific criteria "
                    + "in the request parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "The book is found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Request is invalid", content = @Content),
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        return bookService.searchBooks(bookSearchParametersDto);
    }

    @Operation(summary = "Create new book", description = "This method creates new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The book is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Request is invalid",
                    content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Update book byd id", description = "This method updates "
            + "book by specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The book is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Request is invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The book was not exist by this ID",
                    content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookDto book) {
        return bookService.updateBook(id, book);
    }

    @Operation(summary = "Update book byd id", description = "This method updates "
            + "book by specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The book is deleted"),
            @ApiResponse(responseCode = "400",
                    description = "Request is invalid",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The book was not exist by this ID",
                    content = @Content)
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
