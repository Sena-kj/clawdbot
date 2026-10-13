package com.clawdbot.infra.news;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.nlp.SentimentAnalyzer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptoCompareNewsMapperTest {
    @Test
    void mapsImportanceFromScoreOrVotes() throws Exception {
        String json = """
                {"Data":[
                  {"title":"A","body":"B","source":"reuters","published_on":1700000000,"score":12.5},
                  {"title":"C","body":"D","source":"foo","published_on":1700000001,"upvotes":5,"downvotes":1}
                ]}
                """;

        CryptoCompareNewsMapper mapper = new CryptoCompareNewsMapper(new ObjectMapper());
        SentimentAnalyzer analyzer = text -> 0.2;

        List<NewsItem> items = mapper.map(json, analyzer);

        assertEquals(2, items.size());
        assertEquals(12.5, items.get(0).getImportanceScore());
        assertTrue(items.get(1).getImportanceScore() >= 4.0);
    }
}




