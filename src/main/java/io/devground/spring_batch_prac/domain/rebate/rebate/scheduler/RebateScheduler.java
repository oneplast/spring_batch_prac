package io.devground.spring_batch_prac.domain.rebate.rebate.scheduler;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import io.devground.spring_batch_prac.domain.rebate.rebate.service.RebateBatchService;
import io.devground.spring_batch_prac.global.app.AppConfig;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RebateScheduler {

	private final RebateBatchService rebateBatchService;

	@Scheduled(cron = "0 0 1 15 * *")
	public void runMakeRebateData() {
		if (AppConfig.isNotProd()) {
			return;
		}

		String yearMonth = LocalDateTime.now().minusMonths(1).toString().substring(0, 7);

		rebateBatchService.make(yearMonth);
	}
}
