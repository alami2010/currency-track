package com.api.prices.crypto.cryptoprices.utils;

import org.apache.log4j.LogManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.prices.crypto.cryptoprices.service.PriceService;


@Component
@EnableScheduling
public class SchedulingTasks {

    @Autowired
    private PriceService priceService;

    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);

    private final long SEGUNDO = 1000;
    private final long MINUTE = SEGUNDO * 60;
    
    @Scheduled(fixedRate = MINUTE *10)
    public void reportPrice() {


    	 priceService.initMonitoringOfPrice();

    }
	
}
