package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.USD;
import com.api.prices.crypto.cryptoprices.entity.TableHtml;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HTMLGeneratorService {


    public StringBuffer generateHtmlMessage(List<TableHtml> tableHtmls) {

        StringBuffer sb = new StringBuffer();
        sb.append("<html><body> ");

        String tables = tableHtmls.stream().map(HTMLGeneratorService::generateTableFromListCyrencies).collect(Collectors.joining());

        sb.append(tables);
        sb.append("</body></html>");

        return sb;
    }

    private static StringBuffer generateTableFromListCyrencies(TableHtml tableHtmls) {


        StringBuffer sb = new StringBuffer();

        sb.append("<h4>").append(tableHtmls.getTitle()).append("<h4>");
        sb.append("<table   style='border:1px solid black'> " +
                "<tr><td>ID</td><td>1h</td><td>24h</td><td>7d</td><td>Price</td></tr>");
        String trs = tableHtmls.getCurrencies().stream().map(HTMLGeneratorService::buildMessage).collect(Collectors.joining());
        sb.append(trs);
        sb.append("</table>");
        return sb;
    }


    private static StringBuffer buildMessage(Currency currency) {
        StringBuffer sb = new StringBuffer();
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

        return sb;
    }


}