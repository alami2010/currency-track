package com.api.prices.crypto.cryptoprices.client;


import com.api.prices.crypto.cryptoprices.client.pojo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinMarketPlaceClient {

    private static final String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
    private static final String apiKey = "a3c5ac9b-1b2d-470b-8a67-a5112f71a981";
    private static final Logger logger = LogManager.getLogger(CoinMarketPlaceClient.class);
    private static final String URL_COIN_TO_TRACK = "https://ydahar.000webhostapp.com/currencies.php";

    @Autowired
    private  RestTemplate restTemplate;


    public CurrencyInformation getOneCurrenciesInfo(String currencies) {

/*
        try {
            CurrencyToTrack[] currencyToTrack = getCurrencyToTrack();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        logger.info("Symbol "+currencies);

        List<NameValuePair> paratmers = new ArrayList<>();
        paratmers.add(new BasicNameValuePair("symbol", currencies));

        try {
            String result = makeAPICall(uri, paratmers);


            Gson g = new Gson();



            CurrencyInformation currencyInformation = g.fromJson(result, CurrencyInformation.class);


            return currencyInformation;
        } catch (IOException e) {
            logger.error("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            logger.error("Error: Invalid URL " + e.toString());
        }

        return null;

    }

    private  CurrencyToTrack[] getCurrencyToTrack() throws IOException {




        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(URL_COIN_TO_TRACK);

        request.setHeader(HttpHeaders.ACCEPT, "application/json");

        CloseableHttpResponse response = client.execute(request);

        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String response_content = EntityUtils.toString(entity);


        return  null;
    }


    public static String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return response_content;
    }

}