package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import com.api.prices.crypto.cryptoprices.repository.PricesRepository;
import com.api.prices.crypto.cryptoprices.utils.SchedulingTasks;
import com.api.prices.crypto.cryptoprices.utils.SendMail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceService {

    public static final int MUTIPLY_BIG_MARGE = 3;
    public static final double MUTIPLY_SMALL_MARGE = 1.3;
    @Autowired
    private CoinMarketPlaceClient pricesRestClient;
    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);


    @Autowired
    private PricesRepository pricesRepository;

    public CurrencyInformation getInformation(String id) {
        return pricesRestClient.getOneCurrenciesInfo(id);
    }


    List<CurrencyToTrack> currencyToTracks = null;


    public void initMonitoringOfPrice() {
        logger.info(" ===> Monitoring price <=== ");

        chargerCurrencyToTrack();


        TrackPriceChangeByRobot();


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

        TrackPriceChangeByRobot();
    }

    private void TrackPriceChangeByRobot() {
        logger.info("Track");
        Gson g = new Gson();
        String jsonCurrencyToTrack = g.toJson(currencyToTracks);
        logger.info(jsonCurrencyToTrack);
    }

    private void chargerCurrencyToTrack() {
        if (currencyToTracks == null) currencyToTracks = getCurrenciesToTracks();
    }

    private CurrencyToTrack getCurrencyToTrackByKey(List<CurrencyToTrack> currencyToTracks, String key) {
        return currencyToTracks.stream().filter(currencyToTrack -> currencyToTrack.getName().equals(key)).findFirst().get();
    }

    private List<CurrencyToTrack> getCurrenciesToTracks() {

        List<CurrencyToTrack> currencyToTracks = null;
        try {

            File file = new ClassPathResource("currencies.json").getFile();


            String currencies = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8")); // if not specified, uses windows-1552 on that platform
            Gson g = new Gson();
            Type listType = new TypeToken<ArrayList<CurrencyToTrack>>() {
            }.getType();


            currencyToTracks = g.fromJson(currencies, listType);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencyToTracks;
    }

    private void checkPrice(CurrencyToTrack currencyToTrack, double priceCurrency, boolean isDecisionChecked, Decision decision) {
        if (isDecisionChecked) {
            alertUser(priceCurrency, currencyToTrack, decision);
            updateCurrencyToTrack(currencyToTrack, decision, priceCurrency);
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
        SendMail.sendMail(alertPrice, id, decision);
    }
}
