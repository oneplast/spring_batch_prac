package io.devground.spring_batch_prac.global.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	@Getter
	private static EntityManager entityManager;

	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		AppConfig.entityManager = entityManager;
	}
}
