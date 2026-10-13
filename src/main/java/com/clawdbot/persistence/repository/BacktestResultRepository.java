package com.clawdbot.persistence.repository;

import com.clawdbot.persistence.entity.BacktestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BacktestResultRepository extends JpaRepository<BacktestResultEntity, Long> {
    Page<BacktestResultEntity> findBySymbolAndStartDateAndEndDate(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}

