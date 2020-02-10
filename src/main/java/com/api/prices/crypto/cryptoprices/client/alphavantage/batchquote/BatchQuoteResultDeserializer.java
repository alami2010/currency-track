package com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote;

import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;
import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.AlphaVantageResultDeserializerHelper;
import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchQuoteResultDeserializer extends JsonDeserializer<BatchQuoteResult> {

  @Override
  public BatchQuoteResult deserialize(
      com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext
  )
      throws IOException, JsonProcessingException {
    BatchQuoteResult batchQuoteResult = new BatchQuoteResult();
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);
    try {
      MetaData metaData = AlphaVantageResultDeserializerHelper.getMetaData(node);

      List<BatchQuote> batchQuotes = new ArrayList<>();
      node.fields().forEachRemaining(nodeEntry -> {
        // ignore meta data, we want the time series data
        if (!nodeEntry.getKey().equals("Meta Data")) {
          nodeEntry.getValue().forEach(batchQuote -> {
            try {
              batchQuotes.add(
                  JsonParser.toObject(
                      JsonParser.toJson(AlphaVantageResultDeserializerHelper.sanitizeNodeKeys(batchQuote)),
                      BatchQuote.class
                  )
              );
            }
            catch (IOException e) {
              e.printStackTrace();
            }
          });
        }
      });
      batchQuoteResult.setBatchQuotes(batchQuotes);
      batchQuoteResult.setMetaData(metaData);
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return batchQuoteResult;
  }
}
