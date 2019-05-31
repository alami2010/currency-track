package com.api.prices.crypto.cryptoprices.utils;

import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.USD;
import com.api.prices.crypto.cryptoprices.service.PriceService;

import java.util.stream.Stream;

public class TemplateHTmlGenerator {
    private final PriceService priceService;

    public TemplateHTmlGenerator(PriceService priceService) {
        this.priceService = priceService;
    }

    public StringBuffer generateHtmlMessage(Stream<Currency> currencyStream) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body> <h1>")
                .append(currencyStream.count())
                .append("</h1>\""
                        + "<table style='border:2px solid black'> " +
                        "<tr><td>ID</td><td>1h</td><td>24h</td><td>7d</td><td>Price</td></tr>");


        currencyStream.forEach(currency -> buildMessage(sb, currency));
        sb.append("</table></body></html>");

        return sb;
    }


    private void buildMessage(StringBuffer sb, Currency currency) {

        USD usd = currency.getQuote().getUSD();

        sb.append("<tr bgcolor=\"#33CC99\">")
                .append("<td>")
                .append(currency.getSymbol())
                .append("</td><td>")
                .append(usd.getPercent_change_1h())
                .append("</td><td>")
                .append(usd.getPercent_change_24h())
                .append("</td><td>")
                .append(usd.getPercent_change_7d())
                .append("</td><td>")
                .append(usd.getPrice())
                .append("</td>")
                .append("</tr>");


    }

}