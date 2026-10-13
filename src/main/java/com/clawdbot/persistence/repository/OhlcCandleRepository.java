package com.clawdbot.persistence.repository;

import com.clawdbot.persistence.entity.OhlcCandleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OhlcCandleRepository extends JpaRepository<OhlcCandleEntity, Long> {
}




