package com.clawdbot.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class BacktestMetricDto {
    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int newsCount;
    private final double avgSentiment;
    private final double avgImportance;
    private final double tradeVolume;
    private final double startClose;
    private final double endClose;
    private final double priceChangePercent;
    private final double weightVectorScore;
    private final String decision;
    private final OffsetDateTime createdAt;

    public BacktestMetricDto(
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




