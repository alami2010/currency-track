package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyToTrackService {

    private static Logger logger = LogManager.getLogger(CurrencyToTrackService.class);
    private Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private Gson gson = new Gson();

    public CurrencyToTrackService() {
    }

    List<CurrencyToTrack> getCurrenciesToTracks() {

        List<CurrencyToTrack> currencyToTracks = null;
        try {

            File file = new ClassPathResource("currencies.json").getFile();

            String currencies = new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8")); // if not specified, uses windows-1552 on that platform
            Type listType = new TypeToken<ArrayList<CurrencyToTrack>>() {
            }.getType();

            currencyToTracks = gson.fromJson(currencies, listType);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencyToTracks;
    }


    void TrackPriceChangeByRobot(List<CurrencyToTrack> currencyToTracks) {
        logger.info("Track");
        String jsonCurrencyToTrack = gson.toJson(currencyToTracks);
        logger.info(jsonCurrencyToTrack);

    }

    public void saveNewCurrenciesToTrack(List<CurrencyToTrack> currencyToTracks) {

        try {

            File file = new ClassPathResource("currencies.json").getFile();

            PrintWriter out = new PrintWriter(file);
            String jsonCurrencyToTrack = gsonPretty.toJson(currencyToTracks);
            out.println(jsonCurrencyToTrack);
            out.close();

            logger.error("File saved." + file.getAbsolutePath());

        } catch (IOException e) {
            logger.error("Error creation File : " + e.toString());
        }


    }
}