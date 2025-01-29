package com.example.bookstoreapp.repository.book;

import com.example.bookstoreapp.entity.Book;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    boolean existsById(@NonNull Long id);

    List<Book> findByCategoriesId(Long categoryId);
}
