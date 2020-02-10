package com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange;

import com.api.prices.crypto.cryptoprices.client.alphavantage.request.APIRequest;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.MissingRequiredQueryParameterException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.InvalidSymbolLengthException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.IntradayInterval;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.OutputSize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeignExchangeRequest implements APIRequest {

  @Override
  public String toQueryParameters()
      throws MissingRequiredQueryParameterException,
      InvalidSymbolLengthException {
    if (function == null)
      throw new MissingRequiredQueryParameterException("ForeignExchangeFunction");
    if (fromCurrency == null)
      throw new MissingRequiredQueryParameterException("FromCurrency");
    if (toCurrency == null)
      throw new MissingRequiredQueryParameterException("ToCurrency");
    if (function == ForeignExchangeFunction.FX_INTRADAY
        && intradayInterval == null)
      throw new MissingRequiredQueryParameterException(
          "IntradayInterval", "FX_INTRADAY"
      );

    StringBuilder builder = new StringBuilder();
    builder
        .append("function=")
        .append(function);
    builder
        .append("&from_symbol=")
        .append(fromCurrency);
    builder
        .append("&to_symbol=")
        .append(toCurrency);
    if (outputSize != null) {
      builder
          .append("&outputsize=")
          .append(outputSize);
    }
    if (intradayInterval != null) {
      builder
          .append("&interval=")
          .append(intradayInterval.getQueryParameterKey());
    }
    return builder.toString();
  }

  private ForeignExchangeFunction function;
  private String fromCurrency;
  private String toCurrency;
  private IntradayInterval intradayInterval;
  private OutputSize outputSize;

}
