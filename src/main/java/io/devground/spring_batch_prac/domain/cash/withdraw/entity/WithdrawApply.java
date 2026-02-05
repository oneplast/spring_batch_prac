package io.devground.spring_batch_prac.domain.cash.withdraw.entity;

import java.time.LocalDateTime;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.global.jpa.BaseTime;
import jakarta.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class WithdrawApply extends BaseTime {

	@ManyToOne
	private Member applicant;

	private String bankName;
	private String bankAccountNo;

	private long cash;

	private LocalDateTime withdrawDate;
	private LocalDateTime cancelDate;

	public boolean isWithdrawDone() {
		return withdrawDate != null;
	}
}
