package com.clawdbot.domain;

import java.time.OffsetDateTime;

public class OhlcCandle {
    private final OffsetDateTime openTime;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public OhlcCandle(
            OffsetDateTime openTime,
            double open,
            double high,
            double low,
            double close,
            double volume
    ) {
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public OffsetDateTime getOpenTime() {
        return openTime;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }
}




