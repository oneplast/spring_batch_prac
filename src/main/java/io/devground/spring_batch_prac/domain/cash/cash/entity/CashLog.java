package io.devground.spring_batch_prac.domain.cash.cash.entity;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class CashLog extends BaseTime {

	@ManyToOne
	private Member member;
	@Enumerated(EnumType.STRING)
	private EventType eventType;

	private String relTypeCode;
	private long relId;

	private long price;

	public enum EventType {
		충전__무통장입금,
		충전__토스페이먼츠,
		출금__통장입금,
		사용__토스페이먼츠_주문결제,
		사용__예치금_주문결제,
		환불__예치금_주문결제,
		작가정산__예치금
	}
}
