package io.devground.spring_batch_prac.global.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

public class SecurityUser extends User implements OAuth2User {

	@Getter
	private final long id;

	private final Map<String, Object> attributes;

	public SecurityUser(long id, String username, String password,
		Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
		super(username, password, authorities);
		this.id = id;
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return getUsername();
	}
}
