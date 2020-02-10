package com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange;

import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.AlphaVantageResultDeserializerHelper;
import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Map;

public class CurrencyExchangeResultDeserializer extends JsonDeserializer<CurrencyExchangeResult> {
  @Override
  public CurrencyExchangeResult deserialize(
      com.fasterxml.jackson.core.JsonParser parser, DeserializationContext deserializationContext
  )
      throws IOException, JsonProcessingException {
    CurrencyExchangeResult currencyExchangeResult = new CurrencyExchangeResult();
    ObjectCodec oc = parser.getCodec();
    JsonNode node = oc.readTree(parser);
    try {
      Map<String, Object> sanitizedNodeKeys =
          AlphaVantageResultDeserializerHelper.sanitizeNodeKeys(node.fields().next().getValue());
      CurrencyExchange quote =
          JsonParser.toObject(
              JsonParser.toJson(sanitizedNodeKeys),
              CurrencyExchange.class
          );
      currencyExchangeResult.setQuote(quote);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return currencyExchangeResult;
  }
}
