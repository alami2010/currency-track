package com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries;

import com.api.prices.crypto.cryptoprices.client.alphavantage.request.IntradayInterval;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.OutputSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeSeriesRequestTest {

    @Test
    public void allRequestFieldsArePresentInQueryParameters()
            throws MissingRequiredQueryParameterException {
        TimeSeriesRequest request = TimeSeriesRequest.builder()
                .timeSeriesFunction(TimeSeriesFunction.INTRADAY)
                .intradayInterval(IntradayInterval.ONE_MINUTE)
                .outputSize(OutputSize.COMPACT)
                .symbol("TEST")
                .build();

        String queryParameters = request.toQueryParameters();

        assertEquals(
                queryParameters,
                "function=TIME_SERIES_INTRADAY&symbol=TEST&outputsize=COMPACT&interval=1min"
        );
    }

    @Test
    public void optionalQueryParametersAreNotIncludedInQueryParameters()
            throws MissingRequiredQueryParameterException {
        TimeSeriesRequest request = TimeSeriesRequest.builder()
                .timeSeriesFunction(TimeSeriesFunction.DAILY)
                .symbol("TEST")
                .build();

        String queryParameters = request.toQueryParameters();

        assertEquals(
                queryParameters,
                "function=TIME_SERIES_DAILY&symbol=TEST"
        );
    }





}