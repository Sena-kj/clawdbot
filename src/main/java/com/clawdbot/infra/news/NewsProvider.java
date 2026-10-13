package com.clawdbot.infra.news;

import com.clawdbot.domain.NewsItem;

import java.time.LocalDate;
import java.util.List;

public interface NewsProvider {
    List<NewsItem> fetchNews(LocalDate startDate, LocalDate endDate, String symbol);
}




