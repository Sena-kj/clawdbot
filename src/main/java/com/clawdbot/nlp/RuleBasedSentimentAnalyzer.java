package com.clawdbot.nlp;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RuleBasedSentimentAnalyzer implements SentimentAnalyzer {
    @Override
    public double analyze(String text) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }
        String normalized = text.toLowerCase(Locale.ROOT);
        if (normalized.contains("ban") || normalized.contains("hack") || normalized.contains("lawsuit")) {
            return -0.6;
        }
        if (normalized.contains("approve") || normalized.contains("etf") || normalized.contains("adoption")) {
            return 0.6;
        }
        return 0.0;
    }
}




