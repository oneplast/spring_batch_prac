package io.devground.spring_batch_prac.domain.product.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
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
	private final MemberService memberService;

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

	@Transactional
	public void payByCashOnly(Order order) {
		Member buyer = order.getBuyer();
		long restCash = buyer.getRestCash();
		long payPrice = order.calcPayPrice();

		if (payPrice > restCash) {
			throw new RuntimeException("예치금이 부족합니다.");
		}

		memberService.addCash(buyer, payPrice * -1, CashLog.EventType.사용__예치금_주문결제, order);

		payDone(order);
	}

	public void payDone(Order order) {
		order.setPaymentDone();
	}

	@Transactional
	public void refund(Order order) {
		long payPrice = order.calcPayPrice();

		memberService.addCash(order.getBuyer(), payPrice, CashLog.EventType.환불__예치금_주문결제, order);

		order.setCancelDate();
		order.setRefundDate();
	}
}
