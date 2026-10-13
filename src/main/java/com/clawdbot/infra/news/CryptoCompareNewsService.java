package com.clawdbot.infra.news;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.nlp.SentimentAnalyzer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Primary
@Service
public class CryptoCompareNewsService implements NewsProvider {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final CryptoCompareNewsMapper newsMapper;
    private final SentimentAnalyzer sentimentAnalyzer;

    public CryptoCompareNewsService(
            @Value("${clawdbot.cryptocompare.base-url}") String baseUrl,
            @Value("${clawdbot.cryptocompare.api-key}") String apiKey,
            ObjectMapper objectMapper,
            SentimentAnalyzer sentimentAnalyzer
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.newsMapper = new CryptoCompareNewsMapper(objectMapper);
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public List<NewsItem> fetchNews(LocalDate startDate, LocalDate endDate, String symbol) {
        if (apiKey == null || apiKey.isBlank()) {
            return List.of();
        }

        String categories = resolveCategories(symbol);
        String url = String.format("%s/data/v2/news/?categories=%s&lang=EN", baseUrl, categories);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("authorization", "Apikey " + apiKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return List.of();
            }
            return filterByDate(parseItems(response.body()), startDate, endDate);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return List.of();
        } catch (IOException ex) {
            return List.of();
        }
    }

    private List<NewsItem> parseItems(String body) {
        try {
            return newsMapper.map(body, sentimentAnalyzer);
        } catch (IOException ex) {
            return List.of();
        }
    }

    private List<NewsItem> filterByDate(List<NewsItem> items, LocalDate startDate, LocalDate endDate) {
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : items) {
            LocalDate date = item.getPublishedAt().toLocalDate();
            if ((date.isAfter(startDate) || date.isEqual(startDate))
                    && (date.isBefore(endDate) || date.isEqual(endDate))) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private String resolveCategories(String symbol) {
        if (symbol == null) {
            return "BTC,ETH";
        }
        String normalized = symbol.toUpperCase();
        if (normalized.contains("BTC")) {
            return "BTC";
        }
        if (normalized.contains("ETH")) {
            return "ETH";
        }
        return "BTC,ETH";
    }

}

