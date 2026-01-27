package io.devground.spring_batch_prac.global.rq;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.global.rsdata.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	private Member member;

	public String redirect(String url, String msg) {
		String[] urlBits = url.split("#", 2);
		url = urlBits[0];
		msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);

		StringBuilder sb = new StringBuilder();

		sb.append("redirect:");
		sb.append(url);

		if (StringUtils.hasText(msg)) {
			sb.append("?msg=");
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
}
