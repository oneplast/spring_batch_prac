package io.devground.spring_batch_prac.domain.product.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
