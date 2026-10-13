package com.clawdbot.persistence.repository;

import com.clawdbot.persistence.entity.BacktestSignalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BacktestSignalRepository extends JpaRepository<BacktestSignalEntity, Long> {
    Page<BacktestSignalEntity> findBySymbolAndStartDateAndEndDate(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}

