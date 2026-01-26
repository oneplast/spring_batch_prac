package io.devground.spring_batch_prac.domain.product.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.cart.repository.CartItemRepository;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

	private final CartItemRepository cartItemRepository;

	@Transactional
	public CartItem addItem(Member buyer, Product product) {

		CartItem cartItem = CartItem.builder()
			.buyer(buyer)
			.product(product)
			.build();

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	public List<CartItem> findItemsByBuyer(Member buyer) {

		return cartItemRepository.findByBuyer(buyer);
	}

	public void delete(CartItem cartItem) {

		cartItemRepository.delete(cartItem);
	}
}
