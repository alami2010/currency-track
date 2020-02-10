package com.api.prices.crypto.cryptoprices.client.alphavantage.request;

import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.MissingRequiredQueryParameterException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.InvalidSymbolLengthException;

public interface APIRequest {

  /**
   * Convert all the variables in the request to query parameters for the
   * Alpha Vantage API.
   * @return A list of query parameters.
   */
  String toQueryParameters()
      throws MissingRequiredQueryParameterException,
      InvalidSymbolLengthException;

}
