package com.clawdbot.backtest;

import java.time.LocalDate;

public class BacktestResult {
    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double weightVectorScore;
    private final double priceChangePercent;
    private final String explanation;
    private final DecisionType decision;

    public BacktestResult(
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            double weightVectorScore,
            double priceChangePercent,
            String explanation,
            DecisionType decision
    ) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weightVectorScore = weightVectorScore;
        this.priceChangePercent = priceChangePercent;
        this.explanation = explanation;
        this.decision = decision;
    }

    public static BacktestResult empty(String symbol, LocalDate startDate, LocalDate endDate, String reason) {
        String explanation = "Resultado parcial: " + reason;
        return new BacktestResult(symbol, startDate, endDate, 0.0, 0.0, explanation, DecisionType.HOLD);
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

    public DecisionType getDecision() {
        return decision;
    }
}

