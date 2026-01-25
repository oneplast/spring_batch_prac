package io.devground.spring_batch_prac.domain.cash.cash.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.cash.cash.repository.CashLogRepository;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.global.jpa.BaseEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {

	private final CashLogRepository cashLogRepository;

	@Transactional
	public CashLog addCash(Member member, long price, CashLog.EventType eventType, BaseEntity relEntity) {

		CashLog cashLog = CashLog.builder()
			.member(member)
			.price(price)
			.relTypeCode(relEntity.getModelName())
			.relId(relEntity.getId())
			.eventType(eventType)
			.build();

		cashLogRepository.save(cashLog);

		return cashLog;
	}
}
