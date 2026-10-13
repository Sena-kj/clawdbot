package com.clawdbot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ohlc_candle")
public class OhlcCandleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "open_time", nullable = false)
    private OffsetDateTime openTime;

    @Column(nullable = false)
    private double open;

    @Column(nullable = false)
    private double high;

    @Column(nullable = false)
    private double low;

    @Column(nullable = false)
    private double close;

    @Column(nullable = false)
    private double volume;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected OhlcCandleEntity() {
    }

    public OhlcCandleEntity(
            String symbol,
            OffsetDateTime openTime,
            double open,
            double high,
            double low,
            double close,
            double volume,
            OffsetDateTime createdAt
    ) {
        this.symbol = symbol;
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.createdAt = createdAt;
    }
}




