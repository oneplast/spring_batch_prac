package io.devground.spring_batch_prac.domain.rebate.rebate.batch;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.product.order.repository.OrderItemRepository;
import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;
import io.devground.spring_batch_prac.domain.rebate.rebate.repository.RebateItemRepository;
import io.devground.spring_batch_prac.standard.util.Ut;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MakeRebateDataJobConfig {

	private final int CHUNK_SIZE = 20;
	private final OrderItemRepository orderItemRepository;
	private final RebateItemRepository rebateItemRepository;

	@Bean
	public Job makeRebateDataJob(JobRepository jobRepository, Step makeRebateDataStep1) {
		return new JobBuilder("makeRebateDataJob", jobRepository)
			.start(makeRebateDataStep1)
			.build();
	}

	@JobScope
	@Bean
	public Step makeRebateDataStep1(
		JobRepository jobRepository,
		ItemReader<OrderItem> step1Reader,
		ItemProcessor<OrderItem, RebateItem> step1Processor,
		ItemWriter<RebateItem> step1Writer,
		PlatformTransactionManager platformTransactionManager
	) {
		return new StepBuilder("makeRebateDataStep1", jobRepository)
			.<OrderItem, RebateItem>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(step1Reader)
			.processor(step1Processor)
			.writer(step1Writer)
			.build();
	}

	@StepScope
	@Bean
	public ItemReader<OrderItem> step1Reader(
		@Value("#{jobParameters['startDate']}") String _startDate,
		@Value("#{jobParameters['endDate']}") String _endDate
	) {
		LocalDateTime startDate = Ut.date.parse(_startDate);
		LocalDateTime endDate = Ut.date.parse(_endDate);

		return new RepositoryItemReaderBuilder<OrderItem>()
			.name("step1Reader")
			.repository(orderItemRepository)
			.methodName("findByOrderPayDateBetweenAndOrderRefundDateAndRebateItem")
			.pageSize(CHUNK_SIZE)
			.arguments(Arrays.asList(startDate, endDate, null, null))
			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<OrderItem, RebateItem> step1Processor() {
		return orderItem -> RebateItem.builder()
			.payDate(orderItem.getOrder().getPayDate())
			.eventDate(orderItem.getOrder().getPayDate())
			.rebateRate(orderItem.getRebateRate())
			.payPrice(orderItem.getPayPrice())
			.rebatePrice((long) Math.ceil(orderItem.getPayPrice() * orderItem.getRebateRate()))
			.orderItem(orderItem)
			.buyer(orderItem.getOrder().getBuyer())
			.seller(orderItem.getProduct().getMaker())
			.product(orderItem.getProduct())
			.build();
	}

	@StepScope
	@Bean
	public ItemWriter<RebateItem> step1Writer() {
		return items -> items.forEach(rebateItemRepository::save);
	}
}
