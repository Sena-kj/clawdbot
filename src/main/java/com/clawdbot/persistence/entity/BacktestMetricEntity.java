package com.clawdbot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "backtest_metric")
public class BacktestMetricEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "news_count", nullable = false)
    private int newsCount;

    @Column(name = "avg_sentiment", nullable = false)
    private double avgSentiment;

    @Column(name = "avg_importance", nullable = false)
    private double avgImportance;

    @Column(name = "trade_volume", nullable = false)
    private double tradeVolume;

    @Column(name = "start_close", nullable = false)
    private double startClose;

    @Column(name = "end_close", nullable = false)
    private double endClose;

    @Column(name = "price_change_percent", nullable = false)
    private double priceChangePercent;

    @Column(name = "weight_vector_score", nullable = false)
    private double weightVectorScore;

    @Column(nullable = false)
    private String decision;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected BacktestMetricEntity() {
    }

    public BacktestMetricEntity(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            int newsCount,
            double avgSentiment,
            double avgImportance,
            double tradeVolume,
            double startClose,
            double endClose,
            double priceChangePercent,
            double weightVectorScore,
            String decision,
            OffsetDateTime createdAt
    ) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.newsCount = newsCount;
        this.avgSentiment = avgSentiment;
        this.avgImportance = avgImportance;
        this.tradeVolume = tradeVolume;
        this.startClose = startClose;
        this.endClose = endClose;
        this.priceChangePercent = priceChangePercent;
        this.weightVectorScore = weightVectorScore;
        this.decision = decision;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public double getAvgSentiment() {
        return avgSentiment;
    }

    public double getAvgImportance() {
        return avgImportance;
    }

    public double getTradeVolume() {
        return tradeVolume;
    }

    public double getStartClose() {
        return startClose;
    }

    public double getEndClose() {
        return endClose;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public double getWeightVectorScore() {
        return weightVectorScore;
    }

    public String getDecision() {
        return decision;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

