package com.api.prices.crypto.cryptoprices.client;


import com.api.prices.crypto.cryptoprices.client.pogo.CurrencyInformation;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoinMarketPlaceClient {

    private static final String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
    private static final String apiKey = "a3c5ac9b-1b2d-470b-8a67-a5112f71a981";
    private static final Logger logger = LogManager.getLogger(CoinMarketPlaceClient.class);

    public static void main(String[] args) {


        //getOneCurrencyInfo("BTC");


    }


    public  CurrencyInformation getOneCurrencyInfo(String currency) {
        List<Integer> num = Arrays.asList(1, 2, 3, 4, 5);
        logger.info("Hello from Log4j 2 - num : xx");

        List<NameValuePair> paratmers = new ArrayList<>();
        paratmers.add(new BasicNameValuePair("symbol", currency));

        try {
            String result = makeAPICall(uri, paratmers);
            // System.out.println(result);


            Gson g = new Gson();
            result = result.replaceFirst(currency, "currency");
            CurrencyInformation currencyInformation = g.fromJson(result, CurrencyInformation.class);


            System.out.println(currencyInformation);
            return currencyInformation;
        } catch (IOException e) {
            System.out.println("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        }

        return null;

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