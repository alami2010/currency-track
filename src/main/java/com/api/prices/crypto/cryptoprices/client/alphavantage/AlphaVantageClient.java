package com.api.prices.crypto.cryptoprices.client.alphavantage;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.BatchQuoteRequest;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.BatchQuoteResult;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.BatchQuoteResultDeserializer;
import com.api.prices.crypto.cryptoprices.client.alphavantage.batchquote.InvalidSymbolLengthException;
import com.api.prices.crypto.cryptoprices.client.alphavantage.configuration.AlphaVantageClientConfiguration;
import com.api.prices.crypto.cryptoprices.client.alphavantage.configuration.DataType;
import com.api.prices.crypto.cryptoprices.client.alphavantage.configuration.IAlphaVantageClient;
import com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange.CurrencyExchange;
import com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange.CurrencyExchangeRequest;
import com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange.CurrencyExchangeResult;
import com.api.prices.crypto.cryptoprices.client.alphavantage.currencyexchange.CurrencyExchangeResultDeserializer;
import com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange.ForeignExchangeFunction;
import com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange.ForeignExchangeRequest;
import com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange.ForeignExchangeResult;
import com.api.prices.crypto.cryptoprices.client.alphavantage.foreignexchange.ForeignExchangeResultDeserializer;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.IntradayInterval;
import com.api.prices.crypto.cryptoprices.client.alphavantage.request.OutputSize;
import com.api.prices.crypto.cryptoprices.client.alphavantage.sector.SectorResult;
import com.api.prices.crypto.cryptoprices.client.alphavantage.sector.SectorResultDeserializer;
import com.api.prices.crypto.cryptoprices.client.alphavantage.timeseries.*;
import com.api.prices.crypto.cryptoprices.client.alphavantage.utils.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class AlphaVantageClient implements IAlphaVantageClient {

    private AlphaVantageClientConfiguration configuration;

    @Autowired
    private CoinMarketPlaceClient coinMarketPlaceClient;




    public AlphaVantageClient(AlphaVantageClientConfiguration configuration) {
        this.configuration = configuration;
        JsonParser.addDeserializer(TimeSeriesResult.class, new TimeSeriesResultDeserializer());
        JsonParser.addDeserializer(BatchQuoteResult.class, new BatchQuoteResultDeserializer());
        JsonParser.addDeserializer(CurrencyExchangeResult.class, new CurrencyExchangeResultDeserializer());
        JsonParser.addDeserializer(ForeignExchangeResult.class, new ForeignExchangeResultDeserializer());
        JsonParser.addDeserializer(SectorResult.class, new SectorResultDeserializer());
    }

    @Override
    public TimeSeriesResult getTimeSeries(
            IntradayInterval intradayInterval, String symbol
    )
            throws IOException, MissingRequiredQueryParameterException {
        return getTimeSeries(intradayInterval, symbol, OutputSize.COMPACT);
    }

    @Override
    public TimeSeriesResult getTimeSeries(
            IntradayInterval intradayInterval, String symbol, OutputSize outputSize
    )
            throws IOException, MissingRequiredQueryParameterException {
        String queryParameters = TimeSeriesRequest.builder()
                .timeSeriesFunction(TimeSeriesFunction.INTRADAY)
                .intradayInterval(intradayInterval)
                .symbol(symbol)
                .outputSize(outputSize)
                .build()
                .toQueryParameters();
        return sendAPIRequest(queryParameters, TimeSeriesResult.class);
    }

    @Override
    public TimeSeriesResult getTimeSeries(TimeSeriesFunction timeSeriesFunction, String symbol)
            throws IOException, MissingRequiredQueryParameterException {
        return getTimeSeries(timeSeriesFunction, symbol, OutputSize.COMPACT);
    }

    @Override
    public TimeSeriesResult getTimeSeries(TimeSeriesFunction timeSeriesFunction, String symbol, OutputSize outputSize)
            throws IOException, MissingRequiredQueryParameterException {
        String queryParameters = TimeSeriesRequest.builder()
                .timeSeriesFunction(timeSeriesFunction)
                .symbol(symbol)
                .outputSize(outputSize)
                .build()
                .toQueryParameters();
        return sendAPIRequest(queryParameters, TimeSeriesResult.class);
    }

    @Override
    public BatchQuoteResult getBatchQuote(String... symbols)
            throws MissingRequiredQueryParameterException,
            InvalidSymbolLengthException, IOException {
        String queryParameters = BatchQuoteRequest.builder()
                .symbols(symbols)
                .build()
                .toQueryParameters();
        return sendAPIRequest(queryParameters, BatchQuoteResult.class);
    }

    @Override
    public CurrencyExchange getCurrencyExchange(String fromCurrency, String toCurrency)
            throws MissingRequiredQueryParameterException,
            InvalidSymbolLengthException, IOException {
        String queryParameters = CurrencyExchangeRequest.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .build()
                .toQueryParameters();
        return sendAPIRequest(queryParameters, CurrencyExchangeResult.class)
                .getQuote();
    }

    public ForeignExchangeResult getForeignExchange(
            ForeignExchangeFunction function, String fromCurrency, String toCurrency
    )
            throws MissingRequiredQueryParameterException,
            InvalidSymbolLengthException, IOException {
        String queryParameters = ForeignExchangeRequest.builder()
                .function(function)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .build()
                .toQueryParameters();
        return sendAPIRequest(queryParameters, ForeignExchangeResult.class);
    }

    public SectorResult getSectorPerformances()
            throws IOException {
        String queryParameters = "function=SECTOR";
        return sendAPIRequest(queryParameters, SectorResult.class);
    }

    /**
     * Append the API Key and the DataType to the query parameters and send the
     * API request to Alpha Vantage.
     *
     * @param queryParameters The query parameter string from the Request.
     * @param resultObject    The expected result object from the API.
     * @return The Result of the API request.
     */
    private <T> T sendAPIRequest(String queryParameters, Class<T> resultObject)
            throws IOException {
        queryParameters += "&datatype=" + DataType.JSON;
        queryParameters += "&apikey=" + configuration.getApiKey();
        return JsonParser
                .toObject(Request.sendRequest(queryParameters), resultObject);
    }
}
