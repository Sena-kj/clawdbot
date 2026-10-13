package com.clawdbot.infra.news;

import com.clawdbot.domain.NewsItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NewsApiService implements NewsProvider {
    private final String baseUrl;
    private final String apiKey;

    public NewsApiService(
            @Value("${clawdbot.news.base-url}") String baseUrl,
            @Value("${clawdbot.news.api-key}") String apiKey
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public List<NewsItem> fetchNews(LocalDate startDate, LocalDate endDate, String symbol) {
        // TODO: Integrar NewsAPI ou CryptoCompare com NLP.
        //  - Montar query por symbol e palavras-chave (BTC, Bitcoin, ETH, Ethereum).
        //  - Incluir parametros de data e idioma.
        //  - Mapear para NewsItem com score de sentimento.
        return List.of();
    }
}

