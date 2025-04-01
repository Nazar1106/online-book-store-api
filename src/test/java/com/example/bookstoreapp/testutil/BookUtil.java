package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class BookUtil {

    public static List<BookDto> searchBooksByParams() {
        BookDto book1 = new BookDto();
        book1.setId(1L);
        book1.setTitle("NewBookTitle1");
        book1.setAuthor("NewBookAuthor1");
        book1.setIsbn("NewBookIsbn1");
        book1.setPrice(BigDecimal.valueOf(150));
        book1.setDescription("NewBookDescription1");
        book1.setCoverImage("NewCoverImage1");
        book1.setCategoryIds(List.of(1L));

        List<BookDto> books = new ArrayList<>();
        books.add(book1);

        return books;
    }

    public static Book getBook(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        Category category = new Category();
        category.setId(1L);
        category.setName("Non fiction book");
        category.setDescription("Description");
        category.setDeleted(false);
        book.setCategories(Set.of(category));

        return book;
    }

    public static Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test title");
        book.setAuthor("Test author");
        book.setIsbn("Test isbn");
        book.setPrice(BigDecimal.valueOf(10));
        book.setDescription("Test description");
        book.setCoverImage("Test imagine");

        Category category = new Category();
        category.setId(1L);
        category.setName("Test category name");
        category.setDescription("Test description");
        category.setDeleted(false);
        book.setCategories(Set.of(category));

        return book;
    }

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

    public static BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(List.of(1L));

        return bookDto;
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The !!! Gatsby");
        requestDto.setAuthor("F. Scott Fitzgerald");
        requestDto.setIsbn("97807433273562");
        requestDto.setPrice(BigDecimal.valueOf(16));
        requestDto.setDescription("A novel about the American dream and the tragedy of Jay Gatsby");
        requestDto.setCoverImage("great_gatsby.jpg");

        Category category = new Category();
        category.setId(1L);
        category.setName("Non fiction book");
        category.setDescription("Description");
        category.setDeleted(false);
        requestDto.setCategoryIds(List.of(1L));
        requestDto.setCategoryIds(List.of(category.getId()));

        return requestDto;
    }

    public static Pageable createPageable() {
        return PageRequest.of(0, 10, Sort.by("title"));
    }

    public static Page<Book> createBookPage(Pageable pageable) {
        Book book = getBook(createBookRequestDto());
        return new PageImpl<>(List.of(book), pageable, 1);
    }

    public static CreateBookRequestDto creatBookRequestDto() {
        String title = "title";
        String author = "author";
        String isbn = "isbn";
        String description = "description";
        String coverImage = "image";
        List<Long> categoryIds = List.of(1L);
        BigDecimal price = BigDecimal.valueOf(1.5);

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

    public static BookDto createExpectedBookDto(Long id) {
        String title = "title";
        String author = "author";
        String isbn = "isbn";
        String description = "description";
        String coverImage = "image";
        List<Long> categoryIds = List.of(1L);
        BigDecimal price = BigDecimal.valueOf(1.5);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle(title);
        expectedDto.setAuthor(author);
        expectedDto.setIsbn(isbn);
        expectedDto.setDescription(description);
        expectedDto.setCoverImage(coverImage);
        expectedDto.setCategoryIds(categoryIds);
        expectedDto.setPrice(price);
        return expectedDto;
    }

    public static CreateBookRequestDto getInvalidCreateBookRequestDto() {
        String testTitle = "title";
        String testAuthor = "author";
        String testIsbn = "isbn";
        String description = "description";
        String coverImage = "coverImage";
        List<Long> testCategoryIds = List.of(1L, 1L);
        BigDecimal testPrice = BigDecimal.valueOf(-5);

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle(testTitle);
        createBookRequestDto.setAuthor(testAuthor);
        createBookRequestDto.setIsbn(testIsbn);
        createBookRequestDto.setDescription(description);
        createBookRequestDto.setCoverImage(coverImage);
        createBookRequestDto.setCategoryIds(testCategoryIds);
        createBookRequestDto.setPrice(testPrice);

        return createBookRequestDto;
    }
}
