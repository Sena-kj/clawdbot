package com.clawdbot.infra.news;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.nlp.SentimentAnalyzer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

class CryptoCompareNewsMapper {
    private final ObjectMapper objectMapper;

    CryptoCompareNewsMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    List<NewsItem> map(String body, SentimentAnalyzer sentimentAnalyzer) throws IOException {
        CryptoCompareNewsResponse response = objectMapper.readValue(body, CryptoCompareNewsResponse.class);
        if (response.data == null) {
            return List.of();
        }

        List<NewsItem> items = new ArrayList<>();
        for (CryptoCompareNewsItem item : response.data) {
            OffsetDateTime publishedAt = OffsetDateTime.ofInstant(
                    Instant.ofEpochSecond(item.publishedOn),
                    ZoneOffset.UTC
            );
            String fullText = (item.title == null ? "" : item.title) + " " + (item.body == null ? "" : item.body);
            double sentiment = sentimentAnalyzer.analyze(fullText);
            double importance = calculateImportance(item);
            items.add(new NewsItem(item.title, item.body, item.source, publishedAt, sentiment, importance));
        }
        return items;
    }

    private double calculateImportance(CryptoCompareNewsItem item) {
        if (item.score != null) {
            return Math.max(0.0, item.score);
        }
        double upvotes = item.upvotes == null ? 0.0 : item.upvotes;
        double downvotes = item.downvotes == null ? 0.0 : item.downvotes;
        return Math.max(0.0, upvotes - downvotes);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CryptoCompareNewsResponse {
        @JsonProperty("Data")
        private List<CryptoCompareNewsItem> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CryptoCompareNewsItem {
        @JsonProperty("title")
        private String title;

        @JsonProperty("body")
        private String body;

        @JsonProperty("source")
        private String source;

        @JsonProperty("published_on")
        private long publishedOn;

        @JsonProperty("score")
        private Double score;

        @JsonProperty("upvotes")
        private Integer upvotes;

        @JsonProperty("downvotes")
        private Integer downvotes;
    }
}




