package com.clawdbot.persistence.repository;

import com.clawdbot.persistence.entity.NewsItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface NewsItemRepository extends JpaRepository<NewsItemEntity, Long> {
    Page<NewsItemEntity> findBySymbolAndPublishedAtBetween(
            String symbol,
            OffsetDateTime start,
            OffsetDateTime end,
            Pageable pageable
    );
}

