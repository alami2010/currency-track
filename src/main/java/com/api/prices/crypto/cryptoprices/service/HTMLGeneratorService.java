package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.USD;
import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HTMLGeneratorService {
    private final PriceService priceService;

    public HTMLGeneratorService(PriceService priceService) {
        this.priceService = priceService;
    }

    public StringBuffer generateHtmlMessage(String title,List<Currency> ...currencyStream) {

        StringBuffer sb = new StringBuffer();
        sb.append("<html><body> ") ;

                Arrays.stream(currencyStream).forEach(currencies ->


                generateTableFromListCyrencies(sb, currencies,title)


                );




        sb.append("</body></html>");

        return sb;
    }

    private void generateTableFromListCyrencies(StringBuffer sb, List<Currency> currencies, String tableTitle) {
        sb.append("<h4>").append(tableTitle).append("<h4>");
        sb.append("<table   style='border:1px solid black'> " +
                "<tr><td>ID</td><td>1h</td><td>24h</td><td>7d</td><td>Price</td></tr>");
        currencies.forEach(currency -> buildMessage(sb, currency));
        sb.append("</table>");
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