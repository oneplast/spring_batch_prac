package io.devground.spring_batch_prac.domain.rebate.rebate.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;
import io.devground.spring_batch_prac.domain.rebate.rebate.repository.RebateItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RebateService {

	private final RebateItemRepository rebateItemRepository;
	private final OrderService orderService;

	@Transactional
	public void make(String yearMonth) {

		String[] yearMonthBits = yearMonth.split("-", 2);

		int year = Integer.parseInt(yearMonthBits[0]);
		int month = Integer.parseInt(yearMonthBits[1]);

		YearMonth yearMonth_ = YearMonth.of(year, month);

		LocalDateTime startDate = yearMonth_.atDay(1).atStartOfDay();
		LocalDateTime endDate = yearMonth_.atEndOfMonth().atTime(23, 59, 59);

		List<OrderItem> orderItems = orderService.findByPayDateBetween(startDate, endDate);

		orderItems
			.forEach(oi -> {
					RebateItem rebateItem = RebateItem.builder()
						.orderItem(oi)
						.buyer(oi.getOrder().getBuyer())
						.seller(oi.getProduct().getMaker())
						.product(oi.getProduct())
						.payPrice(oi.getPayPrice())
						.eventDate(oi.getOrder().getPayDate())
						.rebateRate(oi.getRebateRate())
						.rebatePrice((long) Math.ceil(oi.getPayPrice() * oi.getRebateRate()))
						.build();

					rebateItemRepository.save(rebateItem);
				}
			);
	}
}
