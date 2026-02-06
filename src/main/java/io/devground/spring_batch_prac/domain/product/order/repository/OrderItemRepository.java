package io.devground.spring_batch_prac.domain.product.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByOrderPayDateBetweenAndOrderRefundDateOrderByIdDesc(
		LocalDateTime startDate, LocalDateTime endDate, LocalDateTime refundDate
	);
}
