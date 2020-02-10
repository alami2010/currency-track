package com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange;

import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.AlphaVantageResultDeserializerHelper;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class ForeignExchangeResultDeserializer extends JsonDeserializer<ForeignExchangeResult> {
  @Override
  public ForeignExchangeResult deserialize(
      com.fasterxml.jackson.core.JsonParser parser, DeserializationContext deserializationContext
  )
      throws IOException {
    ForeignExchangeResult foreignExchangeResult = new ForeignExchangeResult();
    ObjectCodec oc = parser.getCodec();
    JsonNode node = oc.readTree(parser);
    try {
      MetaData metaData = AlphaVantageResultDeserializerHelper.getMetaData(node);
      Map<Date, ForeignExchange> foreignExchangeQuotes =
          AlphaVantageResultDeserializerHelper.getDateObjectMap(node, ForeignExchange.class);

      foreignExchangeResult.setForeignExchangeQuotes(foreignExchangeQuotes);
      foreignExchangeResult.setMetaData(metaData);
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return foreignExchangeResult;
  }
}
