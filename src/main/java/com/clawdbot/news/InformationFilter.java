package com.clawdbot.news;

import com.clawdbot.domain.NewsItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class InformationFilter {
    private static final Set<String> HYPE_TERMS = Set.of(
            "giveaway",
            "moon",
            "1000x",
            "airdrop",
            "pump",
            "lambo",
            "rug",
            "to the moon"
    );

    private static final Set<String> HIGH_AUTHORITY_SOURCES = Set.of(
            "reuters",
            "bloomberg",
            "sec",
            "fed",
            "federal reserve"
    );

    public List<NewsItem> filterNews(List<NewsItem> items) {
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : items) {
            if (item == null) {
                continue;
            }
            if (isNoise(item)) {
                continue;
            }
            filtered.add(item);
        }
        return filtered;
    }

    public boolean isNoise(NewsItem item) {
        String text = (safe(item.getTitle()) + " " + safe(item.getDescription())).toLowerCase(Locale.ROOT);
        if (containsHypeTerm(text)) {
            return true;
        }
        return !isHighAuthoritySource(item.getSource());
    }

    public boolean containsHypeTerm(String text) {
        for (String term : HYPE_TERMS) {
            if (text.contains(term)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHighAuthoritySource(String source) {
        if (source == null || source.isBlank()) {
            return false;
        }
        String normalized = source.toLowerCase(Locale.ROOT);
        for (String authority : HIGH_AUTHORITY_SOURCES) {
            if (normalized.contains(authority)) {
                return true;
            }
        }
        return false;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

