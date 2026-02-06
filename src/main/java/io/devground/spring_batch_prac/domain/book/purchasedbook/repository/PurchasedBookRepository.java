package io.devground.spring_batch_prac.domain.book.purchasedbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.book.purchasedbook.entity.PurchasedBook;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;

public interface PurchasedBookRepository extends JpaRepository<PurchasedBook, Long> {

	Optional<PurchasedBook> findTop1ByOwnerAndBookOrderByIdDesc(Member buyer, Book book);
}
