package io.devground.spring_batch_prac.domain.product.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
