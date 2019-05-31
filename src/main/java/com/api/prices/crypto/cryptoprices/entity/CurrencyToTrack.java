package com.api.prices.crypto.cryptoprices.entity;

public class CurrencyToTrack {
    String name;
    double min;
    double max;

    public CurrencyToTrack(String name, double min, double max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return this.name + "  " +
                "min=" + min +
                ", max=" + max +
                ", ";
    }
}
