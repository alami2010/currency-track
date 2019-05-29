package com.api.prices.crypto.cryptoprices.utils;

import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SchedulingTasks {

    @Autowired
    private PriceService priceService;

    private static Logger logger = LogManager.getLogger(SchedulingTasks.class);

    private final long SEGUNDO = 1000;
    private final long MINUTE = SEGUNDO * 60;
    private final long HOUR = MINUTE * 60;

    @Scheduled(fixedRate = MINUTE * 10)
    public void reportPrice() {


        priceService.initMonitoringOfPrice();

    }




    @Scheduled(fixedRate = HOUR)
    public void reportStats() {


        priceService.initMonitoringOfStats();

    }

}
