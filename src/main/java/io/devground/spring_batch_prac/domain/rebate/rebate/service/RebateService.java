package io.devground.spring_batch_prac.domain.rebate.rebate.service;

import static org.springframework.http.HttpStatus.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
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
	private final MemberService memberService;

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

	@Transactional
	public void rebate(List<Long> ids) {

		ids.forEach(id -> {
				RebateItem rebateItem = rebateItemRepository.findById(id)
					.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "정산 데이터가 존재하지 않습니다."));

				rebate(rebateItem);
			}
		);
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
