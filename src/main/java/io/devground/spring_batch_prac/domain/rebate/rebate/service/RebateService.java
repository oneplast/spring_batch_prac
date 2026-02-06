package io.devground.spring_batch_prac.domain.rebate.rebate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;
import io.devground.spring_batch_prac.domain.rebate.rebate.repository.RebateItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RebateService {

	private final RebateItemRepository rebateItemRepository;

	@Transactional
	public void make(List<OrderItem> orderItems) {

		orderItems
			.forEach(oi -> {
					RebateItem rebateItem = RebateItem.builder()
						.orderItem(oi)
						.buyer(oi.getOrder().getBuyer())
						.seller(oi.getProduct().getMaker())
						.product(oi.getProduct())
						.payPrice(oi.getPayPrice())
						.eventDate(oi.getOrder().getCreateDate())
						.rebateRate(oi.getRebateRate())
						.build();

					rebateItemRepository.save(rebateItem);
				}
			);
	}
}
