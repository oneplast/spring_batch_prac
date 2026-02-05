package io.devground.spring_batch_prac.domain.cash.withdraw.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	private String msg;

	private LocalDateTime withdrawDate;
	private LocalDateTime cancelDate;

	public String getForPrintWithdrawStatus() {
		if (withdrawDate != null) {
			return "처리완료(" + withdrawDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
		}

		return "처리가능";
	}

	public String getForPrintCancelStatus() {
		if (cancelDate != null) {
			return "취소완료(" + cancelDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
		}

		return "-";
	}

	public void setWithdrawDone() {
		withdrawDate = LocalDateTime.now();
	}

	public void setCancelDone(String msg) {
		cancelDate = LocalDateTime.now();
		this.msg = msg;
	}

	public boolean isWithdrawDone() {
		return withdrawDate != null;
	}

	public boolean isCancelDone() {
		return cancelDate != null;
	}
}
