package com.api.prices.crypto.cryptoprices.client.alphavantage;

import com.api.prices.crypto.cryptoprices.client.alphavantage.request.IntradayInterval;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.OutputSize;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.MissingRequiredQueryParameterException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.TimeSeriesFunction;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.TimeSeriesRequest;

public class TestMain {




    public static void main(String[] args) throws MissingRequiredQueryParameterException {


        TimeSeriesRequest request = TimeSeriesRequest.builder()
                .timeSeriesFunction(TimeSeriesFunction.INTRADAY)
                .intradayInterval(IntradayInterval.ONE_MINUTE)
                .outputSize(OutputSize.COMPACT)
                .symbol("TEST")
                .build();

        String queryParameters = request.toQueryParameters();
        System.out.println(queryParameters);

    }
}
