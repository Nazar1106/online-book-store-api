package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query("update Book b set "
            + "b.title = :#{#book.title}, "
            + "b.author = :#{#book.author}, "
            + "b.isbn = :#{#book.isbn}, "
            + "b.price = :#{#book.price}, "
            + "b.description = :#{#book.description}, "
            + "b.coverImage = :#{#book.coverImage} "
            + "where b.id = :id")
    void updateBookById(@Param("id") Long id,
                        @Param("book") CreateBookRequestDto book);
}
