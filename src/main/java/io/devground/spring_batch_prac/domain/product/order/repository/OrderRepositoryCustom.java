package io.devground.spring_batch_prac.domain.product.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;

public interface OrderRepositoryCustom {

	Page<Order> search(Member buyer, Boolean payStatus, Boolean cancelStatus, Boolean refundStatus, Pageable pageable);
}
