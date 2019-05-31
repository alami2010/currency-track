package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyToTrackService {

    private static Logger logger = LogManager.getLogger(CurrencyToTrackService.class);
    private Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private Gson gson = new Gson();

    public CurrencyToTrackService() {
    }


    void TrackPriceChangeByRobot(List<CurrencyToTrack> currencyToTracks) {
        logger.info("Track");
        String jsonCurrencyToTrack = gson.toJson(currencyToTracks);
        logger.info(jsonCurrencyToTrack);

    }


}