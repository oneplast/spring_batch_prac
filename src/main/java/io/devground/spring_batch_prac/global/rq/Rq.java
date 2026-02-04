package io.devground.spring_batch_prac.global.rq;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import io.devground.spring_batch_prac.global.app.AppConfig;
import io.devground.spring_batch_prac.global.rsdata.RsData;
import io.devground.spring_batch_prac.global.security.SecurityUser;
import io.devground.spring_batch_prac.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final MemberService memberService;

	private Member member;

	public String redirect(String url, String msg) {
		String[] urlBits = url.split("#", 2);
		url = urlBits[0];

		StringBuilder sb = new StringBuilder();

		sb.append("redirect:");
		sb.append(url);

		if (StringUtils.hasText(msg)) {
			msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);

			if (url.contains("?")) {
				sb.append("&msg=");
			} else {
				sb.append("?msg=");
			}

			sb.append(msg);
		}

		if (urlBits.length == 2) {
			sb.append("#");
			sb.append(urlBits[1]);
		}

		return sb.toString();
	}

	public String historyBack(String msg) {
		request.setAttribute("failMsg", msg);

		return "global/js";
	}

	public String redirectOrBack(RsData<?> rs, String path) {
		if (rs.isFail()) {
			return historyBack(rs.getMsg());
		}

		return redirect(path, rs.getMsg());
	}

	public SecurityUser getUser() {
		return (SecurityUser) Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.map(Authentication::getPrincipal)
			.filter(it -> it instanceof SecurityUser)
			.orElse(null);
	}

	public boolean isLogin() {
		return getUser() != null;
	}

	public boolean isLogout() {
		return !isLogin();
	}

	public boolean isAdmin() {
		if (isLogout()) {
			return false;
		}

		return getUser()
			.getAuthorities().stream()
			.anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN"));
	}

	public void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	public String getCurrentQueryStringWithoutParam(String paramName) {
		String queryString = request.getQueryString();

		if (!StringUtils.hasText(queryString)) {
			return "";
		}

		return Ut.url.deleteQueryParam(queryString, paramName);
	}

	public Member getMember() {
		if (isLogout()) {
			return null;
		}

		if (member == null) {
			member = AppConfig.getEntityManager().getReference(Member.class, getUser().getId());
		}

		return member;
	}

	public String getProfileImgUrl() {
		return memberService.getProfileImgUrl(getMember());
	}

	public String getEncodeCurrentUrl() {
		return Ut.url.encode(getCurrentUrl());
	}

	private String getCurrentUrl() {
		String url = request.getRequestURI();
		String queryString = request.getQueryString();

		if (queryString != null) {
			url += "?" + queryString;
		}

		return url;
	}
}
