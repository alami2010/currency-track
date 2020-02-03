package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformationStats;
import com.api.prices.crypto.cryptoprices.entity.BinanceCurrency;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyseService {
    private static Logger logger = LogManager.getLogger(AnalyseService.class);

    @Autowired
    private PriceService priceService;

    @Autowired
    private AlertService alertService;
    @Autowired
    private TrackService currencyTrackService;
    @Autowired
    private CoinMarketPlaceClient pricesRestClient;


    public void initMonitoringOfStats() {
        logger.info(" ===> Start Monitoring Stat <=== ");


        CurrencyInformationStats statCurrencies = pricesRestClient.getStatCurrencies();

        if (statCurrencies != null && statCurrencies.getData() != null) {

            List<Currency> currencies = statCurrencies.getData().stream()
                    .filter(currency -> checkIfBrokerSupportCurrency(currency))
                    .filter(currency -> checkIfCurrencyNeedToBeNotified(currency))
                    .sorted((o1, o2) -> sortedByPercentChange(o1, o2))
                    .collect(Collectors.toList());


            if (!currencies.isEmpty()) {

                alertService.alert("Monitoring Statistique", currencies);
            }
        } else {
            logger.error(" ===> prob  Monitoring Stat <=== " + statCurrencies.getStatus());
        }
        logger.info(" ===> End Monitoring Stat <=== ");
    }

    private int sortedByPercentChange(Currency o1, Currency o2) {
        return (int) (o1.getQuote().getUSD().getPercent_change_7d() - o2.getQuote().getUSD().getPercent_change_7d());
    }


    private boolean checkIfBrokerSupportCurrency(Currency currency) {

        return Arrays.stream(BinanceCurrency.values()).anyMatch((t) -> t.name().equals(currency.getSymbol()));
    }

    private boolean checkIfCurrencyNeedToBeNotified(Currency currency) {

        double percentChange1h = currency.getQuote().getUSD().getPercent_change_1h();
        double percentChange24h = currency.getQuote().getUSD().getPercent_change_24h();
        double percentChange7d = currency.getQuote().getUSD().getPercent_change_7d();

        boolean isDeserveToCheckBy1H = percentChange1h < -10;
        boolean isDeserveToCheckBy24H = percentChange24h < -7;
        boolean isDeserveToCheckBy7D = percentChange7d < -5;

        return ((isDeserveToCheckBy1H || isDeserveToCheckBy24H) && isDeserveToCheckBy7D);
    }
}