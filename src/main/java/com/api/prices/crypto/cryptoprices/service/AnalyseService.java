package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformationStats;
import com.api.prices.crypto.cryptoprices.entity.BinanceCurrency;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.TableHtml;
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
    private AlertService alertService;
    @Autowired
    private CoinMarketPlaceClient pricesRestClient;


    public void alterCurrentCurrency() {
        logger.info(" ===> Monitoring price <=== ");

        List<CurrencyToTrack> currencyToTracks = pricesRestClient.getCurrencyToTrack();
        String currencies = currencyToTracks.stream().map(currencyToTrack -> currencyToTrack.getName()).sorted().collect(Collectors.joining(","));
        CurrencyInformation currencyInfo = pricesRestClient.getOneCurrenciesInfo(currencies);
        if (currencyInfo != null && currencyInfo.getData() != null) {
            List<Currency> currentCurrencies = currencyInfo.getData().values().stream().collect(Collectors.toList());

            currentCurrencies = currentCurrencies.stream().sorted(AnalyseService::sortedByPercentChange).collect(Collectors.toList());

            alertService.alert(new TableHtml("Current currencies",currentCurrencies));
        }
    }


    public void initMonitoringOfStats() {
        logger.info(" ===> Start Monitoring Stat <=== ");


        CurrencyInformationStats statCurrencies = pricesRestClient.getStatCurrencies();

        if (statCurrencies != null && statCurrencies.getData() != null) {

            List<Currency> currencies = statCurrencies.getData().stream()
                    .filter(AnalyseService::checkIfBrokerSupportCurrency)
                    .filter(AnalyseService::checkIfCurrencyNeedToBeNotified)
                    .sorted(AnalyseService::sortedByPercentChange)
                    .collect(Collectors.toList());
            if (!currencies.isEmpty()) {

                alertService.alert(new TableHtml("Monitoring Statistique",currencies));
            }
        } else {
            logger.error(" ===> prob  Monitoring Stat <=== " + statCurrencies.getStatus());
        }
        logger.info(" ===> End Monitoring Stat <=== ");
    }

    public static int sortedByPercentChange(Currency o1, Currency o2) {
        return (int) (o1.getQuote().getUSD().getPercent_change_7d() - o2.getQuote().getUSD().getPercent_change_7d());
    }


    private static boolean checkIfBrokerSupportCurrency(Currency currency) {
        return Arrays.stream(BinanceCurrency.values()).anyMatch((t) -> t.name().equals(currency.getSymbol()));
    }

    private static boolean checkIfCurrencyNeedToBeNotified(Currency currency) {
        double percentChange1h = currency.getQuote().getUSD().getPercent_change_1h();
        double percentChange24h = currency.getQuote().getUSD().getPercent_change_24h();
        double percentChange7d = currency.getQuote().getUSD().getPercent_change_7d();
        boolean isDeserveToCheckBy1H = percentChange1h < -8;
        boolean isDeserveToCheckBy24H = percentChange24h < -7;
        boolean isDeserveToCheckBy7D = percentChange7d < -5;
        return ((isDeserveToCheckBy1H || isDeserveToCheckBy24H) && isDeserveToCheckBy7D);
    }
}