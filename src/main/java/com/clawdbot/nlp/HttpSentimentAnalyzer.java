package com.clawdbot.nlp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Primary
@Component
public class HttpSentimentAnalyzer implements SentimentAnalyzer {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public HttpSentimentAnalyzer(
            ObjectMapper objectMapper,
            @Value("${clawdbot.nlp.base-url:}") String baseUrl
    ) {
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public double analyze(String text) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return 0.0;
        }
        try {
            String body = objectMapper.writeValueAsString(Map.of("text", text));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/analyze"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return 0.0;
            }
            SentimentResponse sentiment = objectMapper.readValue(response.body(), SentimentResponse.class);
            return sentiment.score;
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return 0.0;
        }
    }

    private static class SentimentResponse {
        public double score;
    }
}




