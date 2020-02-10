package com.api.prices.crypto.cryptoprices.client.alphavantage.sector;

import com.api.prices.crypto.cryptoprices.client.alphavantage.response.MetaData;
import lombok.Data;

import java.util.Map;

@Data
public class SectorResult {
  private MetaData metaData;
  private Map<SectorTime, Map<Sector, Double>> timedSectorPerformances;
}
