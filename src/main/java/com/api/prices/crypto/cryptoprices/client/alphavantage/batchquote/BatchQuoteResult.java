package com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote;

import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;
import lombok.Data;

import java.util.List;

@Data
public class BatchQuoteResult {

  private MetaData metaData;
  private List<BatchQuote> batchQuotes;
}
