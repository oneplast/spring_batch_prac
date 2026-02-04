package io.devground.spring_batch_prac.domain.product.order.service;

import static io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog.EventType.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.cart.service.CartService;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;
import io.devground.spring_batch_prac.domain.product.order.repository.OrderRepository;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartService cartService;
	private final MemberService memberService;

	@Transactional
	public Order createFromProduct(Member buyer, Product product) {
		Order order = Order.builder()
			.buyer(buyer)
			.build();

		order.addItem(product);

		orderRepository.save(order);

		return order;
	}

	@Transactional
	public Order createFromOrder(Member buyer) {
		List<CartItem> cartItems = cartService.findByBuyerOrderByIdDesc(buyer);

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
			throw new GlobalException(BAD_REQUEST.value(), "예치금이 부족합니다.");
		}

		memberService.addCash(buyer, payPrice * -1, 사용__예치금_주문결제, order);

		payDone(order);
	}

	@Transactional
	public void payByTossPayments(String orderId, long pgPayPrice) {
		Order order = findByCode(orderId)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 주문입니다."));

		payByTossPayments(order, pgPayPrice);
	}

	@Transactional
	public void payByTossPayments(Order order, long pgPayPrice) {
		Member buyer = order.getBuyer();
		long restCash = buyer.getRestCash();
		long payPrice = order.calcPayPrice();

		long useRestCash = payPrice - pgPayPrice;

		memberService.addCash(buyer, pgPayPrice, 충전__토스페이먼츠, order);
		memberService.addCash(buyer, pgPayPrice * -1, 사용__토스페이먼츠_주문결제, order);

		if (useRestCash > 0) {
			if (useRestCash > restCash) {
				throw new GlobalException(BAD_REQUEST.value(), "예치금이 부족합니다");
			}

			memberService.addCash(buyer, useRestCash * -1, 사용__예치금_주문결제, order);
		}

		payDone(order);
	}

	public void payDone(String code) {
		Order order = findByCode(code)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 주문입니다."));

		payDone(order);
	}

	public void payDone(Order order) {
		order.setPaymentDone();
	}

	@Transactional
	public void cancel(Order order) {
		if (!order.isCancelable()) {
			throw new GlobalException(BAD_REQUEST.value(), "취소할 수 없는 주문입니다.");
		}

		order.setCancelDate();

		if (order.isPayDone()) {
			refund(order);
		}
	}

	public void checkCanPay(String orderCode, long pgPayPrice) {
		Order order = findByCode(orderCode)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 주문입니다."));

		checkCanPay(order, pgPayPrice);
	}

	public void checkCanPay(Order order, long pgPayPrice) {
		if (!canPay(order, pgPayPrice)) {
			throw new GlobalException(BAD_REQUEST.value(), "PG결제금 혹은 예치금 부족으로 인해 결제할 수 없습니다.");
		}
	}

	public boolean canPay(Order order, long pgPayPrice) {
		if (!order.isPayable()) {
			return false;
		}

		long restCash = order.getBuyer().getRestCash();

		return order.calcPayPrice() <= restCash + pgPayPrice;
	}

	public boolean canCancel(Member actor, Order order) {
		return actor.equals(order.getBuyer()) && order.isCancelable();
	}

	public Optional<Order> findById(long id) {
		return orderRepository.findById(id);
	}

	public boolean actorCanSee(Member actor, Order order) {
		return order.getBuyer().equals(actor);
	}

	public Optional<Order> findByCode(String code) {
		long id = Long.parseLong(code.split("__", 2)[1]);

		return findById(id);
	}

	public Page<Order> search(
		Member buyer, Boolean payStatus, Boolean cancelStatus, Boolean refundStatus, Pageable pageable
	) {
		return orderRepository.search(buyer, payStatus, cancelStatus, refundStatus, pageable);
	}

	private void refund(Order order) {
		long payPrice = order.calcPayPrice();

		memberService.addCash(order.getBuyer(), payPrice, 환불__예치금_주문결제, order);

		order.setRefundDate();
	}
}
