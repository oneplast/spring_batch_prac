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

		Member member = Member.builder()
			.username(username)
			.password(password)
			.build();

		memberRepository.save(member);

		return RsData.of(OK.value(), "회원가입 성공", member);
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Transactional
	public void addCash(Member member, long price, CashLog.EventType eventType, BaseEntity relEntity) {

		cashService.addCash(member, price, eventType, relEntity);
	}
}
