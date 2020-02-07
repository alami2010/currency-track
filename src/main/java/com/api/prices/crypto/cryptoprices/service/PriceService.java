package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceService {

    public static final int MULTIPLY_BIG_MARGE = 3;
    public static final double MUTIPLY_SMALL_MARGE = 1.3;
    private static Logger logger = LogManager.getLogger(PriceService.class);
    List<CurrencyToTrack> currencyToTracks = null;
    @Autowired
    private TrackService currencyTrackService;
    @Autowired
    private CoinMarketPlaceClient pricesRestClient;
    @Autowired
    private AlertService alertService;




    public void initMonitoringOfPrice() {
        logger.info(" ===> Monitoring price <=== ");

        chargerCurrencyToTrack();
        currencyTrackService.trackPriceChangeByRobot(currencyToTracks);
        String currencies = currencyToTracks.stream().map(currencyToTrack -> currencyToTrack.getName()).sorted().collect(Collectors.joining(","));
        CurrencyInformation currencyInfo = pricesRestClient.getOneCurrenciesInfo(currencies);
        if (currencyInfo != null && currencyInfo.getData() != null) {
            currencyInfo.getData().entrySet().stream().forEach(currency -> {
                checkPrice(currency);
            });
        }
        currencyTrackService.trackPriceChangeByRobot(currencyToTracks);
    }

    private void checkPrice(Map.Entry<String, Currency> currency) {
        CurrencyToTrack currencyToTrack = getCurrencyToTrackByKey(currencyToTracks, currency.getKey());
        double priceCurrency = currency.getValue().getQuote().getUSD().getPrice();
        String slug = currency.getValue().getSlug();

        checkPrice(currencyToTrack, priceCurrency, priceCurrency >= currencyToTrack.getMax(), Decision.SELL);
        checkPrice(currencyToTrack, priceCurrency, priceCurrency <= currencyToTrack.getMin(), Decision.BUY);

        logger.info(currencyToTrack.toString() + "\t" + priceCurrency + "\t" + StringUtils.capitalize(slug) + "\t\t\t" + currency.getValue().getQuote().getUSD().getPercent_change_1h() + "\t" + currency.getValue().getQuote().getUSD().getPercent_change_24h());

    }


    private void chargerCurrencyToTrack() {
        if (currencyToTracks == null) currencyToTracks = pricesRestClient.getCurrencyToTrack();
    }

    private CurrencyToTrack getCurrencyToTrackByKey(List<CurrencyToTrack> currencyToTracks, String key) {
        return currencyToTracks.stream().filter(currencyToTrack -> currencyToTrack.getName().equals(key)).findFirst().get();
    }


    private void checkPrice(CurrencyToTrack currencyToTrack, double priceCurrency, boolean isDecisionChecked, Decision decision) {
        if (isDecisionChecked) {
            alertService.alert(priceCurrency, currencyToTrack, decision);
            updateCurrencyToTrack(currencyToTrack, decision, priceCurrency);
        }
    }

    private void updateCurrencyToTrack(CurrencyToTrack currencyToTrack, Decision decision, double priceCurrency) {
        double marge;
        switch (decision) {
            case SELL:
                marge = priceCurrency - currencyToTrack.getMax();
                currencyToTrack.setMax(priceCurrency + marge * MULTIPLY_BIG_MARGE);
                currencyToTrack.setMin(currencyToTrack.getMin() + marge * MUTIPLY_SMALL_MARGE);
                break;
            case BUY:
                marge = currencyToTrack.getMin() - priceCurrency;
                currencyToTrack.setMin(priceCurrency - marge * MULTIPLY_BIG_MARGE);
                currencyToTrack.setMax(currencyToTrack.getMax() - marge * MUTIPLY_SMALL_MARGE);

                break;
        }


        pricesRestClient.updateCurrency(currencyToTrack);
    }


}
