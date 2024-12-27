package com.example.bookstoreapp.repository.impl;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private static final String CAN_T_SAVE_BOOK_MSG = "Can't save book";
    private static final String CAN_T_FIND_BOOKS_MSG = "Can't find books";

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(CAN_T_SAVE_BOOK_MSG + book, e);
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Book> fromBook = entityManager
                    .createQuery("FROM Book ", Book.class);
            return fromBook.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(CAN_T_FIND_BOOKS_MSG, e);
        }
    }
}
