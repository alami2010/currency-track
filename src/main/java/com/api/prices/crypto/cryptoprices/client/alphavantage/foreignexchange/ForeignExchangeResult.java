package com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange;

import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ForeignExchangeResult {

  private MetaData metaData;
  private Map<Date, ForeignExchange> foreignExchangeQuotes;

}
