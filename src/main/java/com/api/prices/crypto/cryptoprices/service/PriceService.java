package com.api.prices.crypto.cryptoprices.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import com.api.prices.crypto.cryptoprices.client.pogo.CurrencyInformation;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import com.api.prices.crypto.cryptoprices.utils.SchedulingTasks;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.prices.crypto.cryptoprices.client.PricesRestClient;
import com.api.prices.crypto.cryptoprices.repository.PricesRepository;
import com.api.prices.crypto.cryptoprices.utils.SendMail;
import com.api.prices.crypto.cryptoprices.v1.output.EnableDisableAlertOutput;
import com.api.prices.crypto.cryptoprices.v1.output.PricesCryptoOutput;

@Service
public class PriceService {
	
	@Autowired
	private CoinMarketPlaceClient pricesRestClient;
	private static Logger logger = LogManager.getLogger(SchedulingTasks.class);


	@Autowired
	private PricesRepository pricesRepository;
	
	public CurrencyInformation getInformation(String id) {
		return pricesRestClient.getOneCurrencyInfo(id);
	}


	public void initMonitoringOfPrice() {
		System.out.println("===> Monitoring price <===");

		Arrays.stream(CurrencyToTrack.values()).forEach(currencyToTrack -> {

			CurrencyInformation currencyInfo = pricesRestClient.getOneCurrencyInfo(currencyToTrack.toString());

			if(currencyInfo != null && currencyInfo.getStatus().getError_code() != null){


				String priceCurrency = currencyInfo.getData().getCurrency().getQuote().getUSD().getPrice();
				if(Double.parseDouble(priceCurrency) >= currencyToTrack.getMax()) {
					alertUser(Double.parseDouble(priceCurrency), currencyToTrack,Decision.SELL);
				}
				if(Double.parseDouble(priceCurrency) <= currencyToTrack.getMin()) {
					alertUser(Double.parseDouble(priceCurrency), currencyToTrack,Decision.BUY);
				}




			}

			logger.info(currencyInfo);
			logger.info(currencyToTrack);



		});

	}

	private void alertUser(double alertPrice, CurrencyToTrack id, Decision decision) {
		System.out.println("New price of "+id+" is "+alertPrice);
		SendMail.sendMail(alertPrice,id, decision);
	}
}
