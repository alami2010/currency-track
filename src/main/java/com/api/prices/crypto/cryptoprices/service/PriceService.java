package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import com.api.prices.crypto.cryptoprices.repository.PricesRepository;
import com.api.prices.crypto.cryptoprices.utils.SchedulingTasks;
import com.api.prices.crypto.cryptoprices.utils.SendMail;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PriceService {

    @Autowired
    private CoinMarketPlaceClient pricesRestClient;
    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);


    @Autowired
    private PricesRepository pricesRepository;

    public CurrencyInformation getInformation(String id) {
        return CoinMarketPlaceClient.getOneCurrenciesInfo(id);
    }


    public void initMonitoringOfPrice() {
        System.out.println(" ===> Monitoring price <=== ");


        String currencies = Arrays.stream(CurrencyToTrack.values()).map(currencyToTrack -> currencyToTrack.name()).collect(Collectors.joining(","));

        CurrencyInformation currencyInfo = CoinMarketPlaceClient.getOneCurrenciesInfo(currencies);


        if (currencyInfo != null && currencyInfo.getData() != null) {


            currencyInfo.getData().entrySet().stream().forEach(currency -> {



                CurrencyToTrack currencyToTrack = CurrencyToTrack.valueOf(currency.getKey());

                double priceCurrency = currency.getValue().getQuote().getUSD().getPrice();
                logger.info(currencyToTrack.toString() +" "+  priceCurrency);

                checkPrice(currencyToTrack, priceCurrency, priceCurrency >= currencyToTrack.getMax(), Decision.SELL);
                checkPrice(currencyToTrack, priceCurrency, priceCurrency <= currencyToTrack.getMin(), Decision.BUY);
            });


        }


    }

    private void checkPrice(CurrencyToTrack currencyToTrack, double priceCurrency, boolean isDecisionChecked, Decision sell) {
        if (isDecisionChecked) {
            alertUser(priceCurrency, currencyToTrack, sell);
        }
    }

    private void alertUser(double alertPrice, CurrencyToTrack id, Decision decision) {
        System.out.println("New price of " + id + " is " + alertPrice);
        SendMail.sendMail(alertPrice, id, decision);
    }
}
