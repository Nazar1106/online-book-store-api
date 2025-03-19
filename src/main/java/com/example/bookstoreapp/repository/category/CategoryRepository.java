package com.example.bookstoreapp.repository.category;

import com.example.bookstoreapp.entity.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById(@NonNull Long id);
}
