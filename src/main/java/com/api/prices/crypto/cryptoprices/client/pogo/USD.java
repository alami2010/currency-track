package com.api.prices.crypto.cryptoprices.client.pogo;


public class USD
{
    private String percent_change_1h;

    private String last_updated;

    private String percent_change_24h;

    private String market_cap;

    private String price;

    private String volume_24h;

    private String percent_change_7d;

    public String getPercent_change_1h ()
    {
        return percent_change_1h;
    }

    public void setPercent_change_1h (String percent_change_1h)
    {
        this.percent_change_1h = percent_change_1h;
    }

    public String getLast_updated ()
    {
        return last_updated;
    }

    public void setLast_updated (String last_updated)
    {
        this.last_updated = last_updated;
    }

    public String getPercent_change_24h ()
    {
        return percent_change_24h;
    }

    public void setPercent_change_24h (String percent_change_24h)
    {
        this.percent_change_24h = percent_change_24h;
    }

    public String getMarket_cap ()
    {
        return market_cap;
    }

    public void setMarket_cap (String market_cap)
    {
        this.market_cap = market_cap;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getVolume_24h ()
    {
        return volume_24h;
    }

    public void setVolume_24h (String volume_24h)
    {
        this.volume_24h = volume_24h;
    }

    public String getPercent_change_7d ()
    {
        return percent_change_7d;
    }

    public void setPercent_change_7d (String percent_change_7d)
    {
        this.percent_change_7d = percent_change_7d;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [percent_change_1h = "+percent_change_1h+", last_updated = "+last_updated+", percent_change_24h = "+percent_change_24h+", market_cap = "+market_cap+", price = "+price+", volume_24h = "+volume_24h+", percent_change_7d = "+percent_change_7d+"]";
    }
}

