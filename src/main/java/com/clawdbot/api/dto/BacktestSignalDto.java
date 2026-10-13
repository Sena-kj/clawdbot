package com.clawdbot.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class BacktestSignalDto {
    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String decision;
    private final double weightVectorScore;
    private final OffsetDateTime createdAt;

    public BacktestSignalDto(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            String decision,
            double weightVectorScore,
            OffsetDateTime createdAt
    ) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.decision = decision;
        this.weightVectorScore = weightVectorScore;
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

    public String getDecision() {
        return decision;
    }

    public double getWeightVectorScore() {
        return weightVectorScore;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}




