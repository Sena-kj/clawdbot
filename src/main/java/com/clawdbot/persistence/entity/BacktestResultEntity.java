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
@Table(name = "backtest_result")
public class BacktestResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "weight_vector_score", nullable = false)
    private double weightVectorScore;

    @Column(name = "price_change_percent", nullable = false)
    private double priceChangePercent;

    @Column(nullable = false, length = 2000)
    private String explanation;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected BacktestResultEntity() {
    }

    public BacktestResultEntity(
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

