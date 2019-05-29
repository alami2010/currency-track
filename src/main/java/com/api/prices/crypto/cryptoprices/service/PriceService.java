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

    public CurrencyInformation getInformation(String id) {
        return pricesRestClient.getOneCurrenciesInfo(id);
    }


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

        sendMail.sendMail(text, text);
    }


    public void initMonitoringOfStats() {
        logger.info(" ===> Start Monitoring Stat <=== ");

        StringBuffer sb = new StringBuffer();

        CurrencyInformationStats statCurrencies = pricesRestClient.getStatCurrencies();

        if (statCurrencies != null && statCurrencies.getData() != null) {

            statCurrencies.getData().stream().forEach(currency -> {


                double percentChange1h = currency.getQuote().getUSD().getPercent_change_1h();
                double percentChange24h = currency.getQuote().getUSD().getPercent_change_24h();
                double percentChange7d = currency.getQuote().getUSD().getPercent_change_7d();

                boolean isDeserveToCheckBy1H = percentChange1h < -20 ;
                boolean isDeserveToCheckBy24H = percentChange24h < -15;
                boolean isDeserveToCheckBy7D = percentChange7d < -10;



                if ((isDeserveToCheckBy1H || isDeserveToCheckBy24H ) && isDeserveToCheckBy7D)
                    BuildMessage(sb, currency);

            });

        }


        if (sb.length() > 0) {

            sendMail.sendMail("Monitoring Statistique", sb.toString());
        }

        logger.info(" ===> End Monitoring Stat <=== ");

    }

    private void BuildMessage(StringBuffer sb, Currency currency) {

        final String MESSAGE_TO_SEND = " %1s  1h:%2s  24h:%3s  7d:%4s";
        USD usd = currency.getQuote().getUSD();
        String text = String.format(MESSAGE_TO_SEND, currency.getSymbol(), usd.getPercent_change_1h(), usd.getPercent_change_24h(), usd.getPercent_change_7d());
        sb.append(text).append("\n");
    }
}
