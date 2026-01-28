package io.devground.spring_batch_prac.global.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.authorizeHttpRequests(a -> a
				.requestMatchers("/gen/**").permitAll()
				.requestMatchers("/resource/**").permitAll()
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/adm/**").hasRole("ADMIN")
				.anyRequest().permitAll()
			)
			.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.csrf(c -> c.ignoringRequestMatchers("/h2-console/**"))
			.formLogin(f -> f
				.loginPage("/member/login")
				.defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
				.failureUrl("/member/login?failMsg=" + URLEncoder.encode("아이디 또는 비밀번호가 틀렸습니다", StandardCharsets.UTF_8))
			)
			.oauth2Login(o -> o
				.loginPage("/member/login")
			)
			.logout(l -> l
				.logoutUrl("/member/logout")
			)
		;

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
