package com.clawdbot.api;

import com.clawdbot.api.dto.BacktestMetricDto;
import com.clawdbot.api.dto.BacktestResultDto;
import com.clawdbot.api.dto.BacktestSignalDto;
import com.clawdbot.api.dto.NewsItemDto;
import com.clawdbot.api.dto.PagedResponse;
import com.clawdbot.persistence.entity.BacktestMetricEntity;
import com.clawdbot.persistence.entity.BacktestResultEntity;
import com.clawdbot.persistence.entity.BacktestSignalEntity;
import com.clawdbot.persistence.entity.NewsItemEntity;
import com.clawdbot.persistence.repository.BacktestMetricRepository;
import com.clawdbot.persistence.repository.BacktestResultRepository;
import com.clawdbot.persistence.repository.BacktestSignalRepository;
import com.clawdbot.persistence.repository.NewsItemRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class BacktestQueryService {
    private final BacktestResultRepository backtestResultRepository;
    private final BacktestMetricRepository backtestMetricRepository;
    private final BacktestSignalRepository backtestSignalRepository;
    private final NewsItemRepository newsItemRepository;

    public BacktestQueryService(
            BacktestResultRepository backtestResultRepository,
            BacktestMetricRepository backtestMetricRepository,
            BacktestSignalRepository backtestSignalRepository,
            NewsItemRepository newsItemRepository
    ) {
        this.backtestResultRepository = backtestResultRepository;
        this.backtestMetricRepository = backtestMetricRepository;
        this.backtestSignalRepository = backtestSignalRepository;
        this.newsItemRepository = newsItemRepository;
    }

    public PagedResponse<BacktestResultDto> fetchResults(String symbol, LocalDate start, LocalDate end, int page, int size) {
        Page<BacktestResultEntity> results = backtestResultRepository.findBySymbolAndStartDateAndEndDate(
                symbol,
                start,
                end,
                PageRequest.of(page, size)
        );
        List<BacktestResultDto> content = results.stream().map(this::toResultDto).toList();
        return new PagedResponse<>(content, page, size, results.getTotalElements(), results.getTotalPages());
    }

    public PagedResponse<BacktestMetricDto> fetchMetrics(String symbol, LocalDate start, LocalDate end, int page, int size) {
        Page<BacktestMetricEntity> metrics = backtestMetricRepository.findBySymbolAndStartDateAndEndDate(
                symbol,
                start,
                end,
                PageRequest.of(page, size)
        );
        List<BacktestMetricDto> content = metrics.stream().map(this::toMetricDto).toList();
        return new PagedResponse<>(content, page, size, metrics.getTotalElements(), metrics.getTotalPages());
    }

    public PagedResponse<BacktestSignalDto> fetchSignals(String symbol, LocalDate start, LocalDate end, int page, int size) {
        Page<BacktestSignalEntity> signals = backtestSignalRepository.findBySymbolAndStartDateAndEndDate(
                symbol,
                start,
                end,
                PageRequest.of(page, size)
        );
        List<BacktestSignalDto> content = signals.stream().map(this::toSignalDto).toList();
        return new PagedResponse<>(content, page, size, signals.getTotalElements(), signals.getTotalPages());
    }

    public PagedResponse<NewsItemDto> fetchNews(String symbol, LocalDate start, LocalDate end, int page, int size) {
        OffsetDateTime startAt = start.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endAt = end.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        Page<NewsItemEntity> news = newsItemRepository.findBySymbolAndPublishedAtBetween(
                symbol,
                startAt,
                endAt,
                PageRequest.of(page, size)
        );
        List<NewsItemDto> content = news.stream().map(this::toNewsDto).toList();
        return new PagedResponse<>(content, page, size, news.getTotalElements(), news.getTotalPages());
    }

    private BacktestResultDto toResultDto(BacktestResultEntity entity) {
        return new BacktestResultDto(
                entity.getSymbol(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getWeightVectorScore(),
                entity.getPriceChangePercent(),
                entity.getExplanation(),
                entity.getCreatedAt()
        );
    }

    private BacktestMetricDto toMetricDto(BacktestMetricEntity entity) {
        return new BacktestMetricDto(
                entity.getSymbol(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNewsCount(),
                entity.getAvgSentiment(),
                entity.getAvgImportance(),
                entity.getTradeVolume(),
                entity.getStartClose(),
                entity.getEndClose(),
                entity.getPriceChangePercent(),
                entity.getWeightVectorScore(),
                entity.getDecision(),
                entity.getCreatedAt()
        );
    }

    private BacktestSignalDto toSignalDto(BacktestSignalEntity entity) {
        return new BacktestSignalDto(
                entity.getSymbol(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDecision(),
                entity.getWeightVectorScore(),
                entity.getCreatedAt()
        );
    }

    private NewsItemDto toNewsDto(NewsItemEntity entity) {
        return new NewsItemDto(
                entity.getSymbol(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getSource(),
                entity.getPublishedAt(),
                entity.getSentimentScore(),
                entity.getImportanceScore()
        );
    }
}

