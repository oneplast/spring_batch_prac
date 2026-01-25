package io.devground.spring_batch_prac.domain.book.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
