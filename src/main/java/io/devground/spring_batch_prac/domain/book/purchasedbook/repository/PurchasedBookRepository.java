package io.devground.spring_batch_prac.domain.book.purchasedbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.book.purchasedbook.entity.PurchasedBook;

public interface PurchasedBookRepository extends JpaRepository<PurchasedBook, Long> {
}
