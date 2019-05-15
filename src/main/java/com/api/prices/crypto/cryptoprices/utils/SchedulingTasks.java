package com.api.prices.crypto.cryptoprices.utils;

import com.api.prices.crypto.cryptoprices.client.CoinMarketPlaceClient;
import org.apache.log4j.LogManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.prices.crypto.cryptoprices.repository.PricesRepository;
import com.api.prices.crypto.cryptoprices.service.PriceService;

import java.util.Arrays;
import java.util.List;


@Component
@EnableScheduling
public class SchedulingTasks {


    @Autowired
    private SendMail sendMail;

    @Autowired
    private PriceService priceService;
    @Autowired
    private CoinMarketPlaceClient coinMarketPlaceClient;
    
    @Autowired
    private PricesRepository pricesRepository;
    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);

    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 60;
    
    @Scheduled(fixedRate = MINUTO*10)
    public void reportPrice() {


            logger.info("This is an info message");
            logger.warn("This is a warn message");
            logger.error("This is an error message");
            logger.fatal("This is a fatal message");
            logger.debug("This is a debug message");

        List<Integer> num = Arrays.asList(1, 2, 3, 4, 5);
        logger.info("Hello from Log4j 2 - num : ");



        System.out.println("helowww");
        coinMarketPlaceClient.getOneCurrencyInfo("BTC");

        sendMail.sendMail(55.0);

    	if(pricesRepository.getValueAlert().isStatus()) {
    	//	priceService.initMonitoringOfPrice(pricesRepository.getValueAlert().getPrice(), pricesRepository.getValueAlert().getId());
    	}
    }
	
}
