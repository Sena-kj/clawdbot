package com.clawdbot.backtest;

import com.clawdbot.domain.NewsItem;
import com.clawdbot.domain.OhlcCandle;
import com.clawdbot.infra.binance.BinanceOhlcService;
import com.clawdbot.infra.news.NewsProvider;
import com.clawdbot.news.InformationFilter;
import com.clawdbot.persistence.PersistenceService;
import com.clawdbot.weight.WeightVectorScorer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class WeeklyBacktestService {
    private static final String INTERVAL_1D = "1d";
    private static final double BUY_THRESHOLD = 0.4;
    private static final double SELL_THRESHOLD = -0.4;

    private final BinanceOhlcService binanceOhlcService;
    private final NewsProvider newsProvider;
    private final InformationFilter informationFilter;
    private final WeightVectorScorer weightVectorScorer;
    private final PersistenceService persistenceService;

    public WeeklyBacktestService(
            BinanceOhlcService binanceOhlcService,
            NewsProvider newsProvider,
            InformationFilter informationFilter,
            WeightVectorScorer weightVectorScorer,
            PersistenceService persistenceService
    ) {
        this.binanceOhlcService = binanceOhlcService;
        this.newsProvider = newsProvider;
        this.informationFilter = informationFilter;
        this.weightVectorScorer = weightVectorScorer;
        this.persistenceService = persistenceService;
    }

    public BacktestResult runWeeklyBacktest(LocalDate startDate, LocalDate endDate, String symbol) {
        List<OhlcCandle> candles = binanceOhlcService.getHistoricalOhlc(symbol, startDate, endDate, INTERVAL_1D);
        if (candles.size() < 2) {
            return BacktestResult.empty(symbol, startDate, endDate, "Sem candles suficientes para calcular o movimento.");
        }

        List<NewsItem> rawNews = newsProvider.fetchNews(startDate, endDate, symbol);
        List<NewsItem> filteredNews = informationFilter.filterNews(rawNews);

        double newsVolume = filteredNews.size();
        double tradeVolume = binanceOhlcService.getTradeVolume(symbol, startDate);

        double weightVectorScore = 0.0;
        double topImpactScore = 0.0;
        NewsItem topNews = null;
        for (NewsItem item : filteredNews) {
            double score = weightVectorScorer.score(item, newsVolume, tradeVolume);
            weightVectorScore += score;
            if (Math.abs(score) > Math.abs(topImpactScore)) {
                topImpactScore = score;
                topNews = item;
            }
        }

        double priceChangePercent = calculatePriceChangePercent(candles);
        String mainTitle = topNews == null ? "nenhuma noticia relevante" : topNews.getTitle();
        String movement = String.format(Locale.ROOT, "%.2f%%", priceChangePercent);
        String outcome = String.format(Locale.ROOT, "%.2f%%", priceChangePercent);

        DecisionType decision = decide(weightVectorScore);
        String narrative = String.format(
                Locale.ROOT,
                "Em %s, a noticia \"%s\" causou o movimento de %s. Minha decisao de compra teria resultado em %s hoje.",
                startDate,
                mainTitle,
                movement,
                outcome
        );

        BacktestResult result = new BacktestResult(
                symbol,
                startDate,
                endDate,
                weightVectorScore,
                priceChangePercent,
                narrative,
                decision
        );

        persistenceService.persistBacktest(symbol, candles, filteredNews, result, tradeVolume);
        return result;
    }

    private double calculatePriceChangePercent(List<OhlcCandle> candles) {
        OhlcCandle first = candles.get(0);
        OhlcCandle last = candles.get(candles.size() - 1);
        double startClose = first.getClose();
        double endClose = last.getClose();
        if (startClose == 0.0) {
            return 0.0;
        }
        return ((endClose - startClose) / startClose) * 100.0;
    }

    private DecisionType decide(double weightVectorScore) {
        if (weightVectorScore >= BUY_THRESHOLD) {
            return DecisionType.BUY;
        }
        if (weightVectorScore <= SELL_THRESHOLD) {
            return DecisionType.SELL;
        }
        return DecisionType.HOLD;
    }
}

