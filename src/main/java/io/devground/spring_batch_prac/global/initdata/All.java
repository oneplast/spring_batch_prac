package io.devground.spring_batch_prac.global.initdata;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.global.app.AppConfig;
import lombok.RequiredArgsConstructor;

@Configuration
@Order(2)
@RequiredArgsConstructor
public class All implements ApplicationRunner {

	@Autowired
	@Lazy
	private All self;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		self.work1();
	}

	@Transactional
	public void work1() {
		new File(AppConfig.getTempDirPath()).mkdirs();
	}
}
