package com.clawdbot.persistence;

import com.clawdbot.backtest.BacktestResult;
import com.clawdbot.domain.NewsItem;
import com.clawdbot.domain.OhlcCandle;
import com.clawdbot.persistence.entity.BacktestResultEntity;
import com.clawdbot.persistence.entity.BacktestMetricEntity;
import com.clawdbot.persistence.entity.BacktestSignalEntity;
import com.clawdbot.persistence.entity.NewsItemEntity;
import com.clawdbot.persistence.entity.OhlcCandleEntity;
import com.clawdbot.persistence.repository.BacktestMetricRepository;
import com.clawdbot.persistence.repository.BacktestResultRepository;
import com.clawdbot.persistence.repository.BacktestSignalRepository;
import com.clawdbot.persistence.repository.NewsItemRepository;
import com.clawdbot.persistence.repository.OhlcCandleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersistenceService {
    private final boolean enabled;
    private final BacktestResultRepository backtestResultRepository;
    private final BacktestMetricRepository backtestMetricRepository;
    private final BacktestSignalRepository backtestSignalRepository;
    private final NewsItemRepository newsItemRepository;
    private final OhlcCandleRepository ohlcCandleRepository;

    public PersistenceService(
            @Value("${clawdbot.persistence.enabled:false}") boolean enabled,
            BacktestResultRepository backtestResultRepository,
            BacktestMetricRepository backtestMetricRepository,
            BacktestSignalRepository backtestSignalRepository,
            NewsItemRepository newsItemRepository,
            OhlcCandleRepository ohlcCandleRepository
    ) {
        this.enabled = enabled;
        this.backtestResultRepository = backtestResultRepository;
        this.backtestMetricRepository = backtestMetricRepository;
        this.backtestSignalRepository = backtestSignalRepository;
        this.newsItemRepository = newsItemRepository;
        this.ohlcCandleRepository = ohlcCandleRepository;
    }

    public void persistBacktest(
            String symbol,
            List<OhlcCandle> candles,
            List<NewsItem> newsItems,
            BacktestResult result,
            double tradeVolume
    ) {
        if (!enabled) {
            return;
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<OhlcCandleEntity> candleEntities = new ArrayList<>();
        for (OhlcCandle candle : candles) {
            candleEntities.add(new OhlcCandleEntity(
                    symbol,
                    candle.getOpenTime(),
                    candle.getOpen(),
                    candle.getHigh(),
                    candle.getLow(),
                    candle.getClose(),
                    candle.getVolume(),
                    now
            ));
        }
        ohlcCandleRepository.saveAll(candleEntities);

        List<NewsItemEntity> newsEntities = new ArrayList<>();
        for (NewsItem item : newsItems) {
            newsEntities.add(new NewsItemEntity(
                    symbol,
                    item.getTitle(),
                    item.getDescription(),
                    item.getSource(),
                    item.getPublishedAt(),
                    item.getSentimentScore(),
                    item.getImportanceScore(),
                    now
            ));
        }
        newsItemRepository.saveAll(newsEntities);

        BacktestResultEntity resultEntity = new BacktestResultEntity(
                result.getSymbol(),
                result.getStartDate(),
                result.getEndDate(),
                result.getWeightVectorScore(),
                result.getPriceChangePercent(),
                result.getExplanation(),
                now
        );
        backtestResultRepository.save(resultEntity);

        BacktestSignalEntity signalEntity = new BacktestSignalEntity(
                result.getSymbol(),
                result.getStartDate(),
                result.getEndDate(),
                result.getDecision().name(),
                result.getWeightVectorScore(),
                now
        );
        backtestSignalRepository.save(signalEntity);

        BacktestMetricEntity metricEntity = new BacktestMetricEntity(
                result.getSymbol(),
                result.getStartDate(),
                result.getEndDate(),
                newsItems.size(),
                averageSentiment(newsItems),
                averageImportance(newsItems),
                tradeVolume,
                firstClose(candles),
                lastClose(candles),
                result.getPriceChangePercent(),
                result.getWeightVectorScore(),
                result.getDecision().name(),
                now
        );
        backtestMetricRepository.save(metricEntity);
    }

    private double averageSentiment(List<NewsItem> newsItems) {
        if (newsItems.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (NewsItem item : newsItems) {
            sum += item.getSentimentScore();
        }
        return sum / newsItems.size();
    }

    private double averageImportance(List<NewsItem> newsItems) {
        if (newsItems.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (NewsItem item : newsItems) {
            sum += item.getImportanceScore();
        }
        return sum / newsItems.size();
    }

    private double firstClose(List<OhlcCandle> candles) {
        if (candles.isEmpty()) {
            return 0.0;
        }
        return candles.get(0).getClose();
    }

    private double lastClose(List<OhlcCandle> candles) {
        if (candles.isEmpty()) {
            return 0.0;
        }
        return candles.get(candles.size() - 1).getClose();
    }
}

