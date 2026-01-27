package io.devground.spring_batch_prac.domain.member.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.global.rq.Rq;
import io.devground.spring_batch_prac.global.rsdata.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final Rq rq;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String showJoin() {
		return "domain/member/member/join";
	}

	public record JoinForm(
		@NotBlank
		String username,
		@NotBlank
		String password
	) {
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/join")
	public String join(@Valid JoinForm joinForm) {
		RsData<Member> joinRs = memberService.join(joinForm.username, joinForm.password);

		return rq.redirectOrBack(joinRs, "/member/login");
	}

	@GetMapping("/login")
	public String showLogin() {
		return "domain/member/member/login";
	}
}
