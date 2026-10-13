package com.clawdbot.api;

import com.clawdbot.api.dto.BacktestMetricDto;
import com.clawdbot.api.dto.BacktestResultDto;
import com.clawdbot.api.dto.BacktestSignalDto;
import com.clawdbot.api.dto.NewsItemDto;
import com.clawdbot.api.dto.PagedResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/backtests")
public class BacktestQueryController {
    private final BacktestQueryService queryService;

    public BacktestQueryController(BacktestQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public PagedResponse<BacktestResultDto> getResults(
            @RequestParam String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return queryService.fetchResults(symbol, start, end, page, size);
    }

    @GetMapping("/metrics")
    public PagedResponse<BacktestMetricDto> getMetrics(
            @RequestParam String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return queryService.fetchMetrics(symbol, start, end, page, size);
    }

    @GetMapping("/signals")
    public PagedResponse<BacktestSignalDto> getSignals(
            @RequestParam String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return queryService.fetchSignals(symbol, start, end, page, size);
    }

    @GetMapping("/news")
    public PagedResponse<NewsItemDto> getNews(
            @RequestParam String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return queryService.fetchNews(symbol, start, end, page, size);
    }
}

