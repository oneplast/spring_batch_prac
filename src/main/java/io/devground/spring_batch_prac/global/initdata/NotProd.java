package io.devground.spring_batch_prac.global.initdata;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.book.book.service.BookService;
import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.domain.product.cart.service.CartService;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NotProd implements ApplicationRunner {

	private final MemberService memberService;
	private final BookService bookService;
	private final CartService cartService;
	private final ProductService productService;
	private final OrderService orderService;

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

		Book book1 = bookService.createBook(memberUser1, "책 제목 1", "책 내용 1", 10_000);
		Book book2 = bookService.createBook(memberUser2, "책 제목 2", "책 내용 2", 20_000);
		Book book3 = bookService.createBook(memberUser2, "책 제목 3", "책 내용 3", 30_000);
		Book book4 = bookService.createBook(memberUser3, "책 제목 4", "책 내용 4", 40_000);
		Book book5 = bookService.createBook(memberUser3, "책 제목 5", "책 내용 5", 15_000);
		Book book6 = bookService.createBook(memberUser3, "책 제목 6", "책 내용 6", 20_000);

		Product product1 = productService.createProduct(book3);
		Product product2 = productService.createProduct(book4);
		Product product3 = productService.createProduct(book5);
		Product product4 = productService.createProduct(book5);

		cartService.addCart(memberUser1, product1);
		cartService.addCart(memberUser1, product2);
		cartService.addCart(memberUser1, product3);

		memberService.addCash(memberUser1, 150_000, CashLog.EventType.충전__무통장입금, memberUser1);
		memberService.addCash(memberUser1, -20_000, CashLog.EventType.출금__통장입금, memberUser1);

		Order order1 = orderService.createOrder(memberUser1);

		long order1PayPrice = order1.calcPayPrice();

		orderService.payByCashOnly(order1);
	}
}
