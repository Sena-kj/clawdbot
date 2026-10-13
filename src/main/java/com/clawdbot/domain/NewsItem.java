package com.clawdbot.domain;

import java.time.OffsetDateTime;

public class NewsItem {
    private final String title;
    private final String description;
    private final String source;
    private final OffsetDateTime publishedAt;
    private final double sentimentScore;
    private final double importanceScore;

    public NewsItem(
            String title,
            String description,
            String source,
            OffsetDateTime publishedAt,
            double sentimentScore,
            double importanceScore
    ) {
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishedAt = publishedAt;
        this.sentimentScore = sentimentScore;
        this.importanceScore = importanceScore;
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

