package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformationStats;
import com.api.prices.crypto.cryptoprices.client.pojo.USD;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import com.api.prices.crypto.cryptoprices.repository.PricesRepository;
import com.api.prices.crypto.cryptoprices.utils.SendMail;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PriceService {

    public static final int MUTIPLY_BIG_MARGE = 3;
    public static final double MUTIPLY_SMALL_MARGE = 1.3;


    @Autowired
    private CurrencyToTrackService currencyToTrackService;
    @Autowired
    private CoinMarketPlaceClient pricesRestClient;
    @Autowired
    private SendMail sendMail;
    private static Logger logger = LogManager.getLogger(PriceService.class);
    @Autowired
    private PricesRepository pricesRepository;


    List<CurrencyToTrack> currencyToTracks = null;


    public void initMonitoringOfPrice() {
        logger.info(" ===> Monitoring price <=== ");

        chargerCurrencyToTrack();


        currencyToTrackService.TrackPriceChangeByRobot(currencyToTracks);


        String currencies = currencyToTracks.stream().map(currencyToTrack -> currencyToTrack.getName()).sorted().collect(Collectors.joining(","));

        CurrencyInformation currencyInfo = pricesRestClient.getOneCurrenciesInfo(currencies);


        if (currencyInfo != null && currencyInfo.getData() != null) {


            currencyInfo.getData().entrySet().stream().forEach(currency -> {


                CurrencyToTrack currencyToTrack = getCurrencyToTrackByKey(currencyToTracks, currency.getKey());

                double priceCurrency = currency.getValue().getQuote().getUSD().getPrice();
                String slug = currency.getValue().getSlug();

                logger.info(currencyToTrack.toString() + " " + priceCurrency + " \t" + StringUtils.capitalize(slug));

                checkPrice(currencyToTrack, priceCurrency, priceCurrency >= currencyToTrack.getMax(), Decision.SELL);
                checkPrice(currencyToTrack, priceCurrency, priceCurrency <= currencyToTrack.getMin(), Decision.BUY);
            });


        }

        currencyToTrackService.TrackPriceChangeByRobot(currencyToTracks);
    }


    private void chargerCurrencyToTrack() {
        if (currencyToTracks == null) currencyToTracks = currencyToTrackService.getCurrenciesToTracks();
    }

    private CurrencyToTrack getCurrencyToTrackByKey(List<CurrencyToTrack> currencyToTracks, String key) {
        return currencyToTracks.stream().filter(currencyToTrack -> currencyToTrack.getName().equals(key)).findFirst().get();
    }


    private void checkPrice(CurrencyToTrack currencyToTrack, double priceCurrency, boolean isDecisionChecked, Decision decision) {
        if (isDecisionChecked) {
            alertUser(priceCurrency, currencyToTrack, decision);
            updateCurrencyToTrack(currencyToTrack, decision, priceCurrency);
            currencyToTrackService.saveNewCurrenciesToTrack(currencyToTracks);
        }
    }

    private void updateCurrencyToTrack(CurrencyToTrack currencyToTrack, Decision decision, double priceCurrency) {
        double marge;
        switch (decision) {
            case SELL:
                marge = priceCurrency - currencyToTrack.getMax();
                currencyToTrack.setMax(priceCurrency + marge * MUTIPLY_BIG_MARGE);
                currencyToTrack.setMin(currencyToTrack.getMin() + marge * MUTIPLY_SMALL_MARGE);
                break;
            case BUY:
                marge = currencyToTrack.getMin() - priceCurrency;
                currencyToTrack.setMin(priceCurrency - marge * MUTIPLY_BIG_MARGE);
                currencyToTrack.setMax(currencyToTrack.getMax() - marge * MUTIPLY_SMALL_MARGE);

                break;
        }
    }

    private void alertUser(double alertPrice, CurrencyToTrack id, Decision decision) {
        final String MESSAGE_TO_SEND = "%3s  %1s  price %2s ";

        String text = String.format(MESSAGE_TO_SEND, decision.toString(), id.toString(), String.valueOf(alertPrice));

        sendMail.sendMail(text, text, false);
    }


    public void initMonitoringOfStats() {
        logger.info(" ===> Start Monitoring Stat <=== ");


        CurrencyInformationStats statCurrencies = pricesRestClient.getStatCurrencies();

        if (statCurrencies != null && statCurrencies.getData() != null) {

            Stream<Currency> currencyStream = statCurrencies.getData().stream()
                    .filter(currency -> {


                        double percentChange1h = currency.getQuote().getUSD().getPercent_change_1h();
                        double percentChange24h = currency.getQuote().getUSD().getPercent_change_24h();
                        double percentChange7d = currency.getQuote().getUSD().getPercent_change_7d();

                        boolean isDeserveToCheckBy1H = percentChange1h < -20;
                        boolean isDeserveToCheckBy24H = percentChange24h < -15;
                        boolean isDeserveToCheckBy7D = percentChange7d < -10;

                        return ((isDeserveToCheckBy1H || isDeserveToCheckBy24H) && isDeserveToCheckBy7D);
                    })
                    .sorted((o1, o2) -> (int) (o1.getQuote().getUSD().getPercent_change_7d() - o2.getQuote().getUSD().getPercent_change_7d()));

            StringBuffer sb = generateHtmlMessage(currencyStream);
            if (sb.length() > 0) {

                sendMail.sendMail("Monitoring Statistique", sb.toString(), true);
            }
        } else {

            logger.error(" ===> End Monitoring Stat <=== " + statCurrencies.getStatus());

        }


        logger.info(" ===> End Monitoring Stat <=== ");

    }

    private StringBuffer generateHtmlMessage(Stream<Currency> currencyStream) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body> <h1>This is actual message embedded in HTML tags</h1>\""
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
