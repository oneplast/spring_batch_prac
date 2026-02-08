package io.devground.spring_batch_prac.domain.rebate.rebate.service;

import java.time.YearMonth;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class RebateBatchService {

	private final JobLauncher jobLauncher;
	private final Job makeRebateDataJob;

	@SneakyThrows
	public void make(String yearMonth) {

		String[] yearMonthBits = yearMonth.split("-", 2);

		int year = Integer.parseInt(yearMonthBits[0]);
		int month = Integer.parseInt(yearMonthBits[1]);

		YearMonth yearMonth_ = YearMonth.of(year, month);

		String startDate = yearMonth_ + "-01 00:00:00.000000";
		String endDate = yearMonth_ + "-%02d 23:59:59.999999".formatted(yearMonth_.lengthOfMonth());

		JobParameters jobParameters = new JobParametersBuilder()
			.addString("startDate", startDate)
			.addString("endDate", endDate)
			.toJobParameters();

		jobLauncher.run(makeRebateDataJob, jobParameters);
	}
}
