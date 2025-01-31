package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new category",
            description = "Allows an ADMIN to create a new category in the system",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {@ApiResponse(responseCode = "200",
                    description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data",
                            content = @Content(mediaType = "application/json"))})
    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody @Valid
                                                  CategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get categories by page",
            description = """
                Allows users to retrieve a paginated list of all categories.
                - `size` (optional, default = 20): The number of items per page.
                - `sort` (optional, e.g., `name,asc`).""",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of categories retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponseDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access - the user does not have "
                                    + "the correct authority",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get category by ID",
            description = "Allows users to retrieve a category by its ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {@ApiResponse(responseCode = "200",
                    description = "Category retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access - the user does not have "
                                    + "the correct authority",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get books by category ID",
            description = "Retrieve a list of books that belong to the specified category.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {@ApiResponse(responseCode = "200",
                    description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDtoWithoutCategoryIds.class))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access - the user does not have "
                                    + "the correct authority",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        List<Book> byCategoriesId = bookRepository.findByCategoriesId(id);
        return bookMapper.toDtoWithoutCategories(byCategoriesId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update category by ID",
            description = "Allows an admin to update the details of a category.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {@ApiResponse(responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access - the user does not have "
                                    + "the correct authority",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json"))})
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                              @Valid
                                              @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete category by ID",
            description = "Allows an admin to delete a category by its ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {@ApiResponse(responseCode = "204",
                    description = "Category successfully deleted",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access - the user does not have "
                                    + "the correct authority",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
