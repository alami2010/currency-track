package com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange;

import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.MissingRequiredQueryParameterException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.InvalidSymbolLengthException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.APIRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyExchangeRequest implements APIRequest {

  @Override
  public String toQueryParameters()
      throws MissingRequiredQueryParameterException,
      InvalidSymbolLengthException {
    if (fromCurrency == null)
      throw new MissingRequiredQueryParameterException("FromCurrency");
    if (toCurrency == null)
      throw new MissingRequiredQueryParameterException("ToCurrency");

    StringBuilder builder = new StringBuilder();
    builder
        .append("function=")
        .append(function);
    builder
        .append("&from_currency=")
        .append(fromCurrency);
    builder
        .append("&to_currency=")
        .append(toCurrency);
    return builder.toString();
  }

  private String fromCurrency;
  private String toCurrency;

  private static final String function = "CURRENCY_EXCHANGE_RATE";
}
