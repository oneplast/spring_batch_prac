package io.devground.spring_batch_prac.domain.rebate.rebate.batch;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
public class MakeRebateJobTest {

	@Autowired
	private JobLauncherTestUtils makeRebateDataJobLauncherTestUtils;

	@DisplayName("makeRebateDataJob")
	@Test
	public void t1() throws Exception {
		String startDate = LocalDateTime.now().toString().substring(0, 10) + " 00:00:00.000000";
		String endDate = LocalDateTime.now().toString().substring(0, 10) + " 23:59:59.999999";

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("startDate", startDate)
			.addString("endDate", endDate)
			.toJobParameters();

		makeRebateDataJobLauncherTestUtils.launchJob(jobParameters);
	}
}