package com.clawdbot.weight;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.news.InformationFilter;
import org.springframework.stereotype.Component;

@Component
public class WeightVectorScorer {
    private static final double MAX_ABS_SCORE = 1.0;
    private static final double AUTHORITY_BONUS = 0.35;
    private static final double HYPE_PENALTY = 0.45;
    private static final double IMPORTANCE_MAX_BONUS = 0.3;

    private final InformationFilter informationFilter;

    public WeightVectorScorer(InformationFilter informationFilter) {
        this.informationFilter = informationFilter;
    }

    public double score(NewsItem item, double newsVolume, double tradeVolume) {
        double base = clamp(item.getSentimentScore(), -MAX_ABS_SCORE, MAX_ABS_SCORE);
        double authorityBoost = informationFilter.isHighAuthoritySource(item.getSource()) ? AUTHORITY_BONUS : 0.0;
        double hypePenalty = informationFilter.containsHypeTerm(item.getTitle() + " " + item.getDescription())
                ? HYPE_PENALTY
                : 0.0;
        double importanceBoost = calculateImportanceBoost(item.getImportanceScore());

        double volumeFactor = calculateVolumeFactor(newsVolume, tradeVolume);
        double result = (base + authorityBoost + importanceBoost - hypePenalty) * volumeFactor;

        return clamp(result, -MAX_ABS_SCORE, MAX_ABS_SCORE);
    }

    private double calculateVolumeFactor(double newsVolume, double tradeVolume) {
        if (tradeVolume <= 0.0) {
            return 0.2;
        }
        double ratio = newsVolume / tradeVolume;
        if (ratio < 0.0001) {
            return 0.4;
        }
        if (ratio > 0.01) {
            return 1.1;
        }
        return 0.7;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double calculateImportanceBoost(double importanceScore) {
        if (importanceScore <= 0.0) {
            return 0.0;
        }
        double normalized = importanceScore / 100.0;
        return clamp(normalized, 0.0, IMPORTANCE_MAX_BONUS);
    }
}

