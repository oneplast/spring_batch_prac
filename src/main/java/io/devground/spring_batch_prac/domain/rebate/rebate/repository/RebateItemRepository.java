package io.devground.spring_batch_prac.domain.rebate.rebate.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;

public interface RebateItemRepository extends JpaRepository<RebateItem, Long> {

	List<RebateItem> findByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate);
}
