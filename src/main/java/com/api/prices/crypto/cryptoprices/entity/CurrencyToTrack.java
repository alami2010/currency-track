package com.api.prices.crypto.cryptoprices.entity;

public enum CurrencyToTrack {

    BTC(3000,10000),
    XRP(0.35,0.6),
    ETH(200,300),
    TRX(0.022,0.04),
    ADA(0.06,0.1),
    DENT(0.0008,0.0015),
    MIOTA(0.29,0.5),
    NPXS(0.00055,0.00078),
    NANO(1.3,2),
    XLM(0.09,0.15), //stelar
    XMR(65,100) ; //monero

    double min;
    double max;

     CurrencyToTrack (double min,double max) {
        this.min = min;
        this.max = max;
    }


    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return  this.name()+ "{ " +
                "min=" + min +
                ", max=" + max +
                "}";
    }
}
