package io.devground.spring_batch_prac.domain.cash.withdraw.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.devground.spring_batch_prac.domain.cash.withdraw.entity.WithdrawApply;
import io.devground.spring_batch_prac.domain.cash.withdraw.service.WithdrawService;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/adm/withdraw")
public class AdmWithdrawController {

	private final WithdrawService withdrawService;
	private final Rq rq;

	@GetMapping("/applyList")
	public String showApplyList() {

		List<WithdrawApply> withdrawApplies = withdrawService.findAll();
		rq.attr("withdrawApplies", withdrawApplies);

		return "domain/cash/withdraw/adm/applyList";
	}

	@DeleteMapping("/{id}/cancel")
	public String cancel(@PathVariable long id) {

		WithdrawApply withdrawApply = withdrawService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "출금 신청이 존재하지 않습니다."));

		if (!withdrawService.canCancel(rq.getMember(), withdrawApply)) {
			throw new GlobalException(FORBIDDEN.value(), "출금 신청을 취소할 수 없습니다.");
		}

		withdrawService.cancel(withdrawApply);

		return rq.redirect("/adm/withdraw/applyList", "해당 출금 신청이 취소되었습니다.");
	}

	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable long id) {

		WithdrawApply withdrawApply = withdrawService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "출금 신청이 존재하지 않습니다."));

		if (!withdrawService.canDelete(rq.getMember(), withdrawApply)) {
			throw new GlobalException(FORBIDDEN.value(), "출금 신청을 삭제할 수 없습니다.");
		}

		withdrawService.delete(withdrawApply);

		return rq.redirect("/adm/withdraw/applyList", "해당 출금 신청이 삭제되었습니다.");
	}

	@PostMapping("/{id}/do")
	public String withdraw(@PathVariable long id) {

		WithdrawApply withdrawApply = withdrawService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "출금 신청이 존재하지 않습니다."));

		if (!withdrawService.canWithdraw(rq.getMember(), withdrawApply)) {
			throw new GlobalException(FORBIDDEN.value(), "출금 신청을 취소할 수 없습니다.");
		}

		withdrawService.withdraw(withdrawApply);

		return rq.redirect("/adm/withdraw/applyList", "출금처리가 완료되었습니다.");
	}
}
