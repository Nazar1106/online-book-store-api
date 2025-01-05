package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.entity.Book;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsById(@NonNull Long id);
}
