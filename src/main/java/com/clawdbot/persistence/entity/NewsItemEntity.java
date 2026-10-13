package com.clawdbot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "news_item")
public class NewsItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false, length = 600)
    private String title;

    @Column(length = 4000)
    private String description;

    @Column(nullable = false)
    private String source;

    @Column(name = "published_at", nullable = false)
    private OffsetDateTime publishedAt;

    @Column(name = "sentiment_score", nullable = false)
    private double sentimentScore;

    @Column(name = "importance_score", nullable = false)
    private double importanceScore;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected NewsItemEntity() {
    }

    public NewsItemEntity(
            String symbol,
            String title,
            String description,
            String source,
            OffsetDateTime publishedAt,
            double sentimentScore,
            double importanceScore,
            OffsetDateTime createdAt
    ) {
        this.symbol = symbol;
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishedAt = publishedAt;
        this.sentimentScore = sentimentScore;
        this.importanceScore = importanceScore;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

