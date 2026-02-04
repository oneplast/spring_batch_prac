package io.devground.spring_batch_prac.domain.product.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByBuyer(Member buyer);

	void deleteByBuyerAndProduct(Member buyer, Product product);

	boolean existsByBuyerAndProduct(Member buyer, Product product);
}
