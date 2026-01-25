package io.devground.spring_batch_prac.domain.product.cart.service;

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
	public CartItem addCart(Member member, Product product) {

		CartItem cartItem = CartItem.builder()
			.member(member)
			.product(product)
			.build();

		cartItemRepository.save(cartItem);

		return cartItem;
	}
}
