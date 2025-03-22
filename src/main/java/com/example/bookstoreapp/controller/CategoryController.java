package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.service.CategoryService;
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

@Tag(name = "Category", description = "Provides endpoints for managing book categories")
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Operation(
            summary = "Create a new category",
            description = "Allows users with 'ADMIN' authority "
                    + "to create a new category in the system. "
                    + "The request must contain valid category data."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody @Valid
                                              CategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @Operation(
            summary = "Retrieve paginated categories",
            description = "Fetches a paginated list of categories with optional "
                    + "sorting and page size customization."
                    + " By default, categories are sorted in ascending order."
                    + " The sorting criteria should be specified in the format: "
                    + "sort: field,(ASC||DESC)'"
                    + " Only users with the 'USER' authority have access to this endpoint.."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by ID",
            description = "Allows users with 'USER' authority to retrieve a category by its ID."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Get books by category ID",
            description = "Allows users with 'USER' authority to retrieve a list of books "
                    + "that belong to the specified category."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        List<Book> byCategoriesId = bookRepository.findByCategoriesId(id);
        return bookMapper.toDtoWithoutCategories(byCategoriesId);
    }

    @Operation(summary = "Update category by ID",
            description = "Allows users with 'ADMIN' authority to update the details of a category."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                              @Valid
                                              @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by ID",
            description = "Allows users with 'ADMIN' authority to delete a category by its ID."
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
