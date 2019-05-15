package com.api.prices.crypto.cryptoprices.client.pogo;

import com.google.gson.annotations.SerializedName;

public class Data
{

    @SerializedName("currency")
    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString()
    {
        return "Data [currency = "+currency+"]";
    }
}
