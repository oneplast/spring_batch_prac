package io.devground.spring_batch_prac.domain.rebate.rebate.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MakeRebateJobConfig {

	@Bean
	public JobLauncherTestUtils makeRebateDataJobLauncherTestUtils(Job makeRebateDataJob) {
		JobLauncherTestUtils utils = new JobLauncherTestUtils();
		utils.setJob(makeRebateDataJob);

		return utils;
	}
}
