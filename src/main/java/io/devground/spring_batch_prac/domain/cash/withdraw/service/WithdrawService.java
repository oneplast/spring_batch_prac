package io.devground.spring_batch_prac.domain.cash.withdraw.service;

import static io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog.EventType.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.withdraw.entity.WithdrawApply;
import io.devground.spring_batch_prac.domain.cash.withdraw.repository.WithdrawRepository;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {

	private final WithdrawRepository withdrawRepository;
	private final MemberService memberService;

	@Transactional
	public void apply(Member applicant, long cash, String bankName, String bankAccountNo) {
		WithdrawApply apply = WithdrawApply.builder()
			.applicant(applicant)
			.cash(cash)
			.bankName(bankName)
			.bankAccountNo(bankAccountNo)
			.build();

		withdrawRepository.save(apply);
	}

	@Transactional
	public void cancel(WithdrawApply withdrawApply) {
		withdrawApply.setCancelDone("관리자에 의해 취소됨, 잔액부족");
	}

	@Transactional
	public void delete(WithdrawApply withdrawApply) {
		withdrawRepository.delete(withdrawApply);
	}

	@Transactional
	public void withdraw(WithdrawApply withdrawApply) {
		withdrawApply.setWithdrawDone();

		memberService.addCash(withdrawApply.getApplicant(), -withdrawApply.getCash(), 출금__통장입금, withdrawApply);

		delete(withdrawApply);
	}

	public boolean canApply(Member actor, long cash) {
		return actor.getRestCash() >= cash;
	}

	public boolean canCancel(Member actor, WithdrawApply withdrawApply) {
		if (withdrawApply.isWithdrawDone()) {
			return false;
		}

		if (withdrawApply.isCancelDone()) {
			return false;
		}

		if (!actor.isAdmin()) {
			return false;
		}

		if (withdrawApply.getApplicant().getRestCash() >= withdrawApply.getCash()) {
			return false;
		}

		return true;
	}

	public boolean canDelete(Member actor, WithdrawApply withdrawApply) {
		if (withdrawApply.isWithdrawDone()) {
			return false;
		}

		if (withdrawApply.isCancelDone()) {
			return false;
		}

		if (actor.isAdmin()) {
			return true;
		}

		if (!withdrawApply.getApplicant().equals(actor)) {
			return false;
		}

		return true;
	}

	public boolean canWithdraw(Member actor, WithdrawApply withdrawApply) {
		if (withdrawApply.isWithdrawDone()) {
			return false;
		}

		if (withdrawApply.isCancelDone())
			return false;

		if (!actor.isAdmin()) {
			return false;
		}

		if (withdrawApply.getApplicant().getRestCash() < withdrawApply.getCash()) {
			return false;
		}

		return true;
	}

	public List<WithdrawApply> findAll() {
		return withdrawRepository.findAllByOrderByIdDesc();
	}

	public List<WithdrawApply> findByApplicant(Member applicant) {
		return withdrawRepository.findByApplicantOrderByIdDesc(applicant);
	}

	public Optional<WithdrawApply> findById(long id) {
		return withdrawRepository.findById(id);
	}
}
