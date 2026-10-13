package com.clawdbot.persistence.repository;

import com.clawdbot.persistence.entity.BacktestMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BacktestMetricRepository extends JpaRepository<BacktestMetricEntity, Long> {
    Page<BacktestMetricEntity> findBySymbolAndStartDateAndEndDate(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}

