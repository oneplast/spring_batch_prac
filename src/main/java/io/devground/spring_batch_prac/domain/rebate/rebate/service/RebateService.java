package io.devground.spring_batch_prac.domain.rebate.rebate.service;

import static org.springframework.http.HttpStatus.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;
import io.devground.spring_batch_prac.domain.rebate.rebate.repository.RebateItemRepository;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.standard.util.Ut;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RebateService {

	private final RebateItemRepository rebateItemRepository;
	private final OrderService orderService;
	private final MemberService memberService;

	@Transactional
	public void make(String yearMonth) {

		String[] yearMonthBits = yearMonth.split("-", 2);

		int year = Integer.parseInt(yearMonthBits[0]);
		int month = Integer.parseInt(yearMonthBits[1]);

		YearMonth yearMonth_ = YearMonth.of(year, month);

		LocalDateTime startDate = yearMonth_.atDay(1).atStartOfDay();
		LocalDateTime endDate = yearMonth_.atEndOfMonth().atTime(23, 59, 59);

		List<OrderItem> orderItems = orderService.findNotRebatedAndNotRefundedByPayDateBetween(startDate, endDate);

		orderItems
			.forEach(oi -> {
					RebateItem rebateItem = RebateItem.builder()
						.orderItem(oi)
						.buyer(oi.getOrder().getBuyer())
						.seller(oi.getProduct().getMaker())
						.product(oi.getProduct())
						.payPrice(oi.getPayPrice())
						.eventDate(oi.getOrder().getPayDate())
						.payDate(oi.getOrder().getPayDate())
						.rebateRate(oi.getRebateRate())
						.rebatePrice((long) Math.ceil(oi.getPayPrice() * oi.getRebateRate()))
						.build();

					rebateItemRepository.save(rebateItem);

					oi.setRebateItem(rebateItem);
				}
			);
	}

	@Transactional
	public void rebate(RebateItem rebateItem) {

		if (!rebateItem.isRebateAvailable()) {
			throw new GlobalException(BAD_REQUEST.value(), "정산을 할 수 없는 상태입니다.");
		}

		long rebatePrice = rebateItem.getRebatePrice();

		memberService.addCash(
			rebateItem.getSeller(),
			rebatePrice,
			CashLog.EventType.작가정산__예치금,
			rebateItem
		);

		rebateItem.setRebateDone();
	}

	public List<RebateItem> findByPayDateIn(String yearMonth) {

		int monthEndDay = Ut.date.getEndDayOf(yearMonth);

		String fromDateStr = yearMonth + "-01 00:00:00.000000";
		String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);

		LocalDateTime fromDate = Ut.date.parse(fromDateStr);
		LocalDateTime toDate = Ut.date.parse(toDateStr);

		return rebateItemRepository.findByPayDateBetweenOrderByIdAsc(fromDate, toDate);
	}

	public Optional<RebateItem> findById(long id) {

		return rebateItemRepository.findById(id);
	}

	public boolean canRebate(Member actor, RebateItem rebateItem) {

		return actor.isAdmin() && rebateItem.isRebateAvailable();
	}
}
