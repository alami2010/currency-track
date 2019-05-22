package com.api.prices.crypto.cryptoprices.client.pojo;

import java.util.Map;

public class CurrencyInformation {
    private Map<String, Currency> data;

    private Status status;

    public Map<String, Currency> getData() {
        return data;
    }

    public void setData(Map<String, Currency> data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return " CurrencyInformation [data = " + data + ", status = " + status + "]";
    }
}