package io.devground.spring_batch_prac.domain.book.purchasedbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.book.purchasedbook.entity.PurchasedBook;
import io.devground.spring_batch_prac.domain.book.purchasedbook.repository.PurchasedBookRepository;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchasedBookService {

	private final PurchasedBookRepository purchasedBookRepository;

	@Transactional
	public PurchasedBook add(Member buyer, Book book) {

		PurchasedBook purchasedBook = PurchasedBook.builder()
			.owner(buyer)
			.book(book)
			.build();

		purchasedBookRepository.save(purchasedBook);

		return purchasedBook;
	}

	@Transactional
	public void delete(Member buyer, Book book) {

		purchasedBookRepository.findTop1ByOwnerAndBookOrderByIdDesc(buyer, book)
			.ifPresent(purchasedBookRepository::delete);
	}
}
