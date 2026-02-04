package io.devground.spring_batch_prac.domain.product.cart.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.cart.repository.CartItemRepository;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

	private final CartItemRepository cartItemRepository;

	@Transactional
	public CartItem addItem(Member buyer, Product product) {

		if (buyer.has(product)) {
			throw new GlobalException(HttpStatus.BAD_REQUEST.value(), "이미 구매한 상품입니다.");
		}

		CartItem cartItem = CartItem.builder()
			.buyer(buyer)
			.product(product)
			.build();

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	@Transactional
	public void removeItem(Member buyer, Product product) {
		cartItemRepository.deleteByBuyerAndProduct(buyer, product);
	}

	public List<CartItem> findByBuyer(Member buyer) {

		return cartItemRepository.findByBuyer(buyer);
	}

	@Transactional
	public void delete(CartItem cartItem) {

		cartItemRepository.delete(cartItem);
	}

	public boolean canAdd(Member buyer, Product product) {
		if (Objects.isNull(buyer)) {
			return false;
		}

		return !cartItemRepository.existsByBuyerAndProduct(buyer, product);
	}

	public boolean canRemove(Member buyer, Product product) {
		if (Objects.isNull(buyer)) {
			return false;
		}

		return cartItemRepository.existsByBuyerAndProduct(buyer, product);
	}
}
