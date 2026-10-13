package com.clawdbot.infra.binance;

import com.clawdbot.domain.OhlcCandle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BinanceOhlcService {
    private static final Pattern KLINE_PATTERN = Pattern.compile(
            "\\[(\\d+),\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\""
    );

    private final HttpClient httpClient;
    private final String baseUrl;

    public BinanceOhlcService(@Value("${clawdbot.binance.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<OhlcCandle> getHistoricalOhlc(String symbol, LocalDate startDate, LocalDate endDate, String interval) {
        long startTime = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long endTime = endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        String url = String.format(
                "%s/api/v3/klines?symbol=%s&interval=%s&startTime=%d&endTime=%d",
                baseUrl,
                symbol,
                interval,
                startTime,
                endTime
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return List.of();
            }
            return parseKlines(response.body());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return List.of();
        } catch (IOException ex) {
            return List.of();
        }
    }

    public double getTradeVolume(String symbol, LocalDate date) {
        List<OhlcCandle> candles = getHistoricalOhlc(symbol, date, date, "1d");
        double sum = 0.0;
        for (OhlcCandle candle : candles) {
            sum += candle.getVolume();
        }
        return sum;
    }

    private List<OhlcCandle> parseKlines(String body) {
        List<OhlcCandle> candles = new ArrayList<>();
        Matcher matcher = KLINE_PATTERN.matcher(body);
        while (matcher.find()) {
            long openTime = Long.parseLong(matcher.group(1));
            double open = Double.parseDouble(matcher.group(2));
            double high = Double.parseDouble(matcher.group(3));
            double low = Double.parseDouble(matcher.group(4));
            double close = Double.parseDouble(matcher.group(5));
            double volume = Double.parseDouble(matcher.group(6));

            OffsetDateTime openDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(openTime), ZoneOffset.UTC);
            candles.add(new OhlcCandle(openDateTime, open, high, low, close, volume));
        }
        return candles;
    }
}

