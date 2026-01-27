package io.devground.spring_batch_prac.domain.member.member.service;

import static org.springframework.http.HttpStatus.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;
import io.devground.spring_batch_prac.domain.cash.cash.repository.CashLogRepository;
import io.devground.spring_batch_prac.domain.cash.cash.service.CashService;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.repository.MemberRepository;
import io.devground.spring_batch_prac.global.jpa.BaseEntity;
import io.devground.spring_batch_prac.global.rsdata.RsData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final CashLogRepository cashLogRepository;
	private final CashService cashService;

	@Transactional
	public RsData<Member> join(String username, String password) {

		if (findByUsername(username).isPresent()) {
			return RsData.of(BAD_REQUEST.value(), "이미 존재하는 회원입니다.");
		}

		Member member = Member.builder()
			.username(username)
			.password(password)
			.build();

		memberRepository.save(member);

		return RsData.of(
			OK.value(),
			"%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()),
			member
		);
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Transactional
	public void addCash(Member member, long price, CashLog.EventType eventType, BaseEntity relEntity) {

		CashLog cashLog = cashService.addCash(member, price, eventType, relEntity);

		long newRestCash = member.getRestCash() + cashLog.getPrice();
		member.setRestCash(newRestCash);
	}
}
