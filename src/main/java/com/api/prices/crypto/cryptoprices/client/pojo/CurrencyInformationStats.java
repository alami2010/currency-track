package com.api.prices.crypto.cryptoprices.client.pojo;

import java.util.List;
import java.util.Map;

public class CurrencyInformationStats {
    private List<Currency> data;

    private Status status;

    public List< Currency> getData() {
        return data;
    }

    public void setData(List data) {
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