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

    private BookService bookService;

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
            book.setAuthor("Nazar");
            book.setTitle("test");
            book.setIsbn("test");
            book.setPrice(BigDecimal.ONE);
            book.setCoverImage("test");
            book.setDescription("test");
            bookService.save(book);

            List all = bookService.findAll();
            System.out.println(all);
        };
    }
}
