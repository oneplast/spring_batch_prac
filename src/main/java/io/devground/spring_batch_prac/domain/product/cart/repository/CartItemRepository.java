package io.devground.spring_batch_prac.domain.product.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByBuyer(Member buyer);
}
