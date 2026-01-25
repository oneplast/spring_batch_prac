package io.devground.spring_batch_prac.global.initdata;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.book.book.service.BookService;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NotProd implements ApplicationRunner {

	private final MemberService memberService;
	private final BookService bookService;
	private final ProductService productService;

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		if (memberService.findByUsername("admin").isPresent()) {
			return;
		}

		Member memberAdmin = memberService.join("admin", "1234").getData();
		Member memberUser1 = memberService.join("user1", "1234").getData();
		Member memberUser2 = memberService.join("user2", "1234").getData();
		Member memberUser3 = memberService.join("user3", "1234").getData();

		Book book1 = bookService.createBook(memberUser1, "책 제목 1", "책 내용 1", 10000);
		Book book2 = bookService.createBook(memberUser2, "책 제목 2", "책 내용 2", 10000);
		Book book3 = bookService.createBook(memberUser2, "책 제목 3", "책 내용 3", 10000);
		Book book4 = bookService.createBook(memberUser3, "책 제목 4", "책 내용 4", 10000);
		Book book5 = bookService.createBook(memberUser3, "책 제목 5", "책 내용 5", 10000);
		Book book6 = bookService.createBook(memberUser3, "책 제목 6", "책 내용 6", 10000);

		productService.createBook(book3);
		productService.createBook(book4);
		productService.createBook(book5);
		productService.createBook(book5);
	}
}
