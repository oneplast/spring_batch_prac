package io.devground.spring_batch_prac.domain.cash.withdraw.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.devground.spring_batch_prac.domain.cash.withdraw.entity.WithdrawApply;
import io.devground.spring_batch_prac.domain.cash.withdraw.service.WithdrawService;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
@PreAuthorize("isAuthenticated()")
public class WithdrawController {

	private final WithdrawService withdrawService;
	private final Rq rq;

	@GetMapping("/applyList")
	public String showApplyList() {

		List<WithdrawApply> withdrawApplies = withdrawService.findByApplicant(rq.getMember());
		rq.attr("withdrawApplies", withdrawApplies);

		return "domain/cash/withdraw/applyList";
	}

	@GetMapping("/apply")
	public String showApply() {
		return "domain/cash/withdraw/apply";
	}

	public record ApplyForm(
		@NotNull long cash,
		@NotNull String bankName,
		@NotNull String bankAccountNo
	) {
	}

	@PostMapping("/apply")
	public String apply(@Valid ApplyForm form) {

		if (!withdrawService.canApply(rq.getMember(), form.cash)) {
			throw new GlobalException(BAD_REQUEST.value(), "출금 신청이 불가능합니다.");
		}

		withdrawService.apply(rq.getMember(), form.cash, form.bankName, form.bankAccountNo);

		return rq.redirect("/withdraw/applyList", "출금 신청이 완료되었습니다.");
	}
}
