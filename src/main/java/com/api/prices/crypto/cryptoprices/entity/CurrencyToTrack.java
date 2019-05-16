package com.api.prices.crypto.cryptoprices.entity;

public enum CurrencyToTrack {

    BTC(3000,10000),
    XRP(0.35,0.6),
    ETH(150,300),
    TRX(0.14,0.4),
    ADA(0.4,1),
    DENT(0.0007,0.0015),
    MIOTA(0.26,0.6)
    ;

    double min;
    double max;

    private CurrencyToTrack (double min,double max) {
        this.min = min;
        this.max = max;
    }

}
