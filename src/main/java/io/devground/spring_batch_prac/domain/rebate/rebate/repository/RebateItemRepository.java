package io.devground.spring_batch_prac.domain.rebate.rebate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.rebate.rebate.entity.RebateItem;

public interface RebateItemRepository extends JpaRepository<RebateItem, Long> {
}
