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
@Table(name = "backtest_signal")
public class BacktestSignalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String decision;

    @Column(name = "weight_vector_score", nullable = false)
    private double weightVectorScore;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected BacktestSignalEntity() {
    }

    public BacktestSignalEntity(
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

