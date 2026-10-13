package com.clawdbot.weight;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.news.InformationFilter;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WeightVectorScorerTest {
    @Test
    void importanceIncreasesScore() {
        InformationFilter filter = new InformationFilter();
        WeightVectorScorer scorer = new WeightVectorScorer(filter);

        NewsItem low = new NewsItem(
                "A",
                "B",
                "reuters",
                OffsetDateTime.now(),
                0.0,
                0.0
        );
        NewsItem high = new NewsItem(
                "A",
                "B",
                "reuters",
                OffsetDateTime.now(),
                0.0,
                100.0
        );

        double scoreLow = scorer.score(low, 10, 10000);
        double scoreHigh = scorer.score(high, 10, 10000);

        assertTrue(scoreHigh > scoreLow);
    }
}




