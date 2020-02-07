package com.api.prices.crypto.cryptoprices.entity;

import com.api.prices.crypto.cryptoprices.client.pojo.Currency;

import java.util.List;

public class TableHtml {

    public TableHtml(String title, List<Currency> currencies) {
        this.title = title;
        this.currencies = currencies;
    }

    private String title;
    private List<Currency>  currencies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
