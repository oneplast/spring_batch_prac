package io.devground.spring_batch_prac.global.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberService memberService;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		Map<String, Object> attributes = oAuth2User.getAttributes();

		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = kakaoAccount != null
			? (Map<String, Object>) kakaoAccount.get("profile")
			: null;

		String nickname = profile != null ? (String) profile.get("nickname") : null;
		String profileImageUrl = profile != null ? (String) profile.get("profile_image_url") : null;

		if (nickname == null) {
			Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

			if (properties != null) {
				nickname = (String) properties.get("nickname");
				if (profileImageUrl == null) {
					profileImageUrl = (String) properties.get("profile_image");
				}
			}
		}

		String oauthId = oAuth2User.getName();
		String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
		String username = providerTypeCode + "__" + oauthId;

		Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname, profileImageUrl).getData();

		return new SecurityUser(
			member.getId(),
			member.getUsername(),
			member.getPassword(),
			member.getAuthorities(),
			attributes
		);
	}
}
