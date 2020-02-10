package com.api.prices.crypto.cryptoprices.client.alphavantage.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Required configuration properties in order for the
 * API Client to function.
 */
@Component
public class AlphaVantageClientConfiguration {


    public String getApiKey() {
        return apiKey;
    }

    @Value("${apiKey}")
    private String apiKey;
}
