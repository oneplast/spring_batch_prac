package io.devground.spring_batch_prac.domain.cash.withdraw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.withdraw.entity.WithdrawApply;
import io.devground.spring_batch_prac.domain.cash.withdraw.repository.WithdrawRepository;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {

	private final WithdrawRepository withdrawRepository;

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

	public boolean canApply(Member actor, long cash) {
		return actor.getRestCash() >= cash;
	}

	public List<WithdrawApply> findByApplicant(Member applicant) {
		return withdrawRepository.findByApplicantOrderByIdDesc(applicant);
	}
}
