package com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries;

import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * A representation of the API response for the TimeSeries endpoints.
 */
@Data
public class TimeSeriesResult {
  private MetaData metaData;
  private Map<Date, TimeSeries> timeSeries;
}
