package com.clawdbot.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class BacktestResultDto {
    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double weightVectorScore;
    private final double priceChangePercent;
    private final String explanation;
    private final OffsetDateTime createdAt;

    public BacktestResultDto(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            double weightVectorScore,
            double priceChangePercent,
            String explanation,
            OffsetDateTime createdAt
    ) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weightVectorScore = weightVectorScore;
        this.priceChangePercent = priceChangePercent;
        this.explanation = explanation;
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

    public double getWeightVectorScore() {
        return weightVectorScore;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public String getExplanation() {
        return explanation;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}




