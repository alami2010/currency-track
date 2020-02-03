package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private static Logger logger = LogManager.getLogger(TrackService.class);
    private Gson gson = new Gson();

    public TrackService() {
    }


    void trackPriceChangeByRobot(List<CurrencyToTrack> currencyToTracks) {
        logger.info("Track");
        String jsonCurrencyToTrack = gson.toJson(currencyToTracks);
        logger.info(jsonCurrencyToTrack);

    }


}