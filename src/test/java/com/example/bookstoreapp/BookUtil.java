package com.example.bookstoreapp;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;

import java.math.BigDecimal;
import java.util.List;

public class BookUtil {

    public static BookDto getBookDto(Long testId) {
        String testTitle = "NewBookTitle1";
        String testAuthor = "NewBookAuthor1";
        String testIsbn = "NewBookIsbn1";
        String testDescription = "NewBookDescription1";
        String testCoverImage = "NewCoverImage1";
        List<Long> testCategoryIds = List.of(1L);
        BigDecimal testPrice = BigDecimal.valueOf(150);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(testId);
        expectedDto.setTitle(testTitle);
        expectedDto.setAuthor(testAuthor);
        expectedDto.setIsbn(testIsbn);
        expectedDto.setDescription(testDescription);
        expectedDto.setCoverImage(testCoverImage);
        expectedDto.setCategoryIds(testCategoryIds);
        expectedDto.setPrice(testPrice);
        return expectedDto;
    }

    public static CreateBookRequestDto creatBookRequestDto(
            String title, String author, String isbn, String description,
            String coverImage, List<Long> categoryIds, BigDecimal price) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(title);
        requestDto.setAuthor(author);
        requestDto.setIsbn(isbn);
        requestDto.setDescription(description);
        requestDto.setCoverImage(coverImage);
        requestDto.setCategoryIds(categoryIds);
        requestDto.setPrice(price);
        return requestDto;
    }

    public static BookDto createExpectedBookDto(
            Long id, String title, String author, String isbn, String description,
            String coverImagine, List<Long> categoryIds, BigDecimal price) {
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle(title);
        expectedDto.setAuthor(author);
        expectedDto.setIsbn(isbn);
        expectedDto.setDescription(description);
        expectedDto.setCoverImage(coverImagine);
        expectedDto.setCategoryIds(categoryIds);
        expectedDto.setPrice(price);
        return expectedDto;
    }

}
