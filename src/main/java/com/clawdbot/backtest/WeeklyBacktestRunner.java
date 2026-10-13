package com.clawdbot.backtest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class WeeklyBacktestRunner implements CommandLineRunner {
    private final WeeklyBacktestService weeklyBacktestService;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public WeeklyBacktestRunner(
            WeeklyBacktestService weeklyBacktestService,
            @Value("${clawdbot.backtest.start-date}") LocalDate startDate,
            @Value("${clawdbot.backtest.end-date}") LocalDate endDate
    ) {
        this.weeklyBacktestService = weeklyBacktestService;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void run(String... args) {
        BacktestResult btc = weeklyBacktestService.runWeeklyBacktest(startDate, endDate, "BTCUSDT");
        BacktestResult eth = weeklyBacktestService.runWeeklyBacktest(startDate, endDate, "ETHUSDT");

        System.out.println(formatResult(btc));
        System.out.println(formatResult(eth));
    }

    private String formatResult(BacktestResult result) {
        return String.format(
                "Backtest %s (%s -> %s): peso=%.2f decisao=%s | %s",
                result.getSymbol(),
                result.getStartDate(),
                result.getEndDate(),
                result.getWeightVectorScore(),
                result.getDecision(),
                result.getExplanation()
        );
    }
}

