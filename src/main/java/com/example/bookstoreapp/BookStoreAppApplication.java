package com.example.bookstoreapp;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreAppApplication {

    private static final String TITLE = "1984";
    private static final String AUTHOR = "George Orwell";
    private static final String ISBN = "978-0451524935";
    private static final BigDecimal PRICE = BigDecimal.valueOf(9.99);
    private static final String DESCRIPTION = "A dystopian novel where Winston Smith rebels against"
            + " a totalitarian regime that controls all aspects of life";
    private static final String COVER_IMAGE = "Often features a red background"
            + ", symbolizing oppression.";
    private final BookService bookService;

    @Autowired
    public BookStoreAppApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {

        SpringApplication.run(BookStoreAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle(TITLE);
            book.setAuthor(AUTHOR);
            book.setIsbn(ISBN);
            book.setPrice(PRICE);
            book.setDescription(DESCRIPTION);
            book.setCoverImage(COVER_IMAGE);
            bookService.save(book);

            List<Book> all = bookService.findAll();
            System.out.println(all);
        };
    }
}
