package io.devground.spring_batch_prac.domain.product.cart.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.cart.service.CartService;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.service.ProductService;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

	private final Rq rq;
	private final CartService cartService;
	private final ProductService productService;

	@GetMapping("/list")
	@PreAuthorize("isAuthenticated()")
	public String showList() {
		List<CartItem> cartItems = cartService.findByBuyerOrderByIdDesc(rq.getMember());

		long totalPrice = cartItems.stream()
			.map(CartItem::getProduct)
			.mapToLong(Product::getPrice)
			.sum();

		rq.attr("totalPrice", totalPrice);
		rq.attr("cartItems", cartItems);

		return "domain/product/cart/list";
	}

	@PostMapping("/add/{id}")
	@PreAuthorize("isAuthenticated()")
	public String add(@PathVariable long id, @RequestParam(defaultValue = "/") String redirectUrl) {

		Product product = productService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 상품입니다."));

		cartService.addItem(rq.getMember(), product);

		return rq.redirect(redirectUrl, null);
	}

	@DeleteMapping("/remove/{id}")
	@PreAuthorize("isAuthenticated()")
	public String remove(@PathVariable long id, @RequestParam(defaultValue = "/") String redirectUrl) {

		Product product = productService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 상품입니다."));

		cartService.removeItem(rq.getMember(), product);

		return rq.redirect(redirectUrl, null);
	}
}
