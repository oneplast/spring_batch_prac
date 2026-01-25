package io.devground.spring_batch_prac.domain.book.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.book.book.repository.BookRepository;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;

	@Transactional
	public Book createBook(Member author, String title, String body, int price) {

		Book book = Book.builder()
			.author(author)
			.title(title)
			.body(body)
			.price(price)
			.build();

		bookRepository.save(book);

		return book;
	}
}
