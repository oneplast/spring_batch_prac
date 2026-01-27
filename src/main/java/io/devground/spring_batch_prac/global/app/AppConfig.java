package io.devground.spring_batch_prac.global.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	@Getter
	private static EntityManager entityManager;

	@Getter
	private static String siteName;

	@Value("${custom.site.name}")
	public void setSiteName(String siteName) {
		AppConfig.siteName = siteName;
	}

	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		AppConfig.entityManager = entityManager;
	}
}
