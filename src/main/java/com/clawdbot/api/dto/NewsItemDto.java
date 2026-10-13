package com.clawdbot.api.dto;

import java.time.OffsetDateTime;

public class NewsItemDto {
    private final String symbol;
    private final String title;
    private final String description;
    private final String source;
    private final OffsetDateTime publishedAt;
    private final double sentimentScore;
    private final double importanceScore;

    public NewsItemDto(
            String symbol,
            String title,
            String description,
            String source,
            OffsetDateTime publishedAt,
            double sentimentScore,
            double importanceScore
    ) {
        this.symbol = symbol;
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishedAt = publishedAt;
        this.sentimentScore = sentimentScore;
        this.importanceScore = importanceScore;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public double getImportanceScore() {
        return importanceScore;
    }
}




