package io.devground.spring_batch_prac.domain.product.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByBuyerOrderByIdDesc(Member buyer);

	List<Order> findByBuyerAndPayDateIsNotNullOrderByIdDesc(Member buyer);

	List<Order> findByBuyerAndCancelDateIsNotNullOrderByIdDesc(Member buyer);

	List<Order> findByBuyerAndRefundDateIsNotNullOrderByIdDesc(Member buyer);
}
