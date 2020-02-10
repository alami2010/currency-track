package com.api.prices.crypto.cryptoprices.utils;

import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.InvalidSymbolLengthException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.configuration.IAlphaVantageClient;
import com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange.CurrencyExchange;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.MissingRequiredQueryParameterException;
import com.api.prices.crypto.cryptoprices.service.AnalyseService;
import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SchedulingTasks {

    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);
    private final long SEGUNDO = 1000;
    private final long MINUTE = SEGUNDO * 60;
    private final long HOUR = MINUTE * 60;
    @Autowired
    private PriceService priceService;
    @Autowired
    private AnalyseService analyseService;

    @Autowired
    private IAlphaVantageClient alphaVantageClient;


    @Scheduled(fixedRate = MINUTE * 5)
    public void reportPrice() {
        priceService.initMonitoringOfPrice();
    }

    @Scheduled(fixedRate = HOUR * 6)
    public void reportStats() {
        analyseService.initMonitoringOfStats();

    }


    @Scheduled(fixedRate = HOUR * 6)
    public void etf() throws IOException, MissingRequiredQueryParameterException, InvalidSymbolLengthException {
        CurrencyExchange currencyExchange = alphaVantageClient.getCurrencyExchange("EUR","USD");
        System.out.println(currencyExchange);

    }

}
