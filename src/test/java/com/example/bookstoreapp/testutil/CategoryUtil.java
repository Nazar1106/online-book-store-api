package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Category;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CategoryUtil {

    public static Category createCategory() {
        Long id = 1L;
        String name = "Fiction";
        String description = "Fictional books";
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setDeleted(false);
        return category;
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Books that contain fictional stories");
        category.setDeleted(false);

        return category;
    }

    public static List<CategoryResponseDto> getAllCategories() {
        CategoryResponseDto category = new CategoryResponseDto();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Books that contain fictional stories");

        CategoryResponseDto category2 = new CategoryResponseDto();
        category2.setId(2L);
        category2.setName("Science");
        category2.setDescription("Books related to scientific topics");

        CategoryResponseDto category3 = new CategoryResponseDto();
        category3.setId(3L);
        category3.setName("History");
        category3.setDescription("Books that cover historical events and figures");

        CategoryResponseDto category4 = new CategoryResponseDto();
        category4.setId(4L);
        category4.setName("Technology");
        category4.setDescription("Books about technological advancements and innovations");

        return List.of(category, category2, category3, category4);
    }

    public static CategoryResponseDto createCategoryResponseDto(Category category) {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(category.getId());
        responseDto.setName(category.getName());
        responseDto.setDescription(category.getDescription());
        return responseDto;
    }

    public static CategoryRequestDto updateCategoryRequestDto() {
        String updateName = "Updated Fiction";
        String updateDescription = "Updated description";

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName(updateName);
        requestDto.setDescription(updateDescription);
        return requestDto;
    }

    public static List<BookDtoWithoutCategoryIds> createBooksList() {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setTitle("NewBookTitle1");
        book1.setAuthor("NewBookAuthor1");
        book1.setIsbn("NewBookIsbn1");
        book1.setPrice(BigDecimal.valueOf(150));
        book1.setDescription("NewBookDescription1");
        book1.setCoverImage("NewCoverImage1");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setTitle("NewBookTitle2");
        book2.setAuthor("NewBookAuthor2");
        book2.setIsbn("NewBookIsbn2");
        book2.setPrice(BigDecimal.valueOf(120));
        book2.setDescription("NewBookDescription2");
        book2.setCoverImage("NewCoverImage2");

        return Arrays.asList(book1, book2);
    }

    public static CategoryResponseDto updateCategory(Long id, String name, String description) {
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        return categoryDto;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        String name = "createName";
        String description = "createDescription";
        CategoryRequestDto categoryRequestDtoDto = new CategoryRequestDto();
        categoryRequestDtoDto.setName(name);
        categoryRequestDtoDto.setDescription(description);
        return categoryRequestDtoDto;
    }

    public static CategoryResponseDto expectedCategoryResponseDto() {
        String testName = "Fiction";
        String testDescription = "Books that contain fictional stories";
        Long testId = 1L;

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(testName);
        categoryResponseDto.setDescription(testDescription);
        categoryResponseDto.setId(testId);
        return categoryResponseDto;
    }

    public static CategoryResponseDto expectedNewCategory() {
        Long id = 5L;
        String name = "createName";
        String description = "createDescription";
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(id);
        responseDto.setName(name);
        responseDto.setDescription(description);
        return responseDto;
    }
}
