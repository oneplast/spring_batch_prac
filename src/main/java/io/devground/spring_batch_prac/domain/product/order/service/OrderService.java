package io.devground.spring_batch_prac.domain.product.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.cart.service.CartService;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;
import io.devground.spring_batch_prac.domain.product.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartService cartService;

	@Transactional
	public Order createOrder(Member buyer) {
		List<CartItem> cartItems = cartService.findItemsByBuyer(buyer);

		Order order = Order.builder()
			.buyer(buyer)
			.build();

		cartItems.forEach(order::addItem);

		orderRepository.save(order);

		cartItems.forEach(cartService::delete);

		return order;
	}
}
