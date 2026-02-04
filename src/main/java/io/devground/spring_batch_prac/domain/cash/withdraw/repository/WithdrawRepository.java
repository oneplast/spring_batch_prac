package io.devground.spring_batch_prac.domain.cash.withdraw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.cash.withdraw.entity.WithdrawApply;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;

public interface WithdrawRepository extends JpaRepository<WithdrawApply, Long> {

	List<WithdrawApply> findByApplicantOrderByIdDesc(Member applicant);
}
