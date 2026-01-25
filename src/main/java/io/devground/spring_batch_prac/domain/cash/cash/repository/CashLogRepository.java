package io.devground.spring_batch_prac.domain.cash.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.cash.cash.entity.CashLog;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
