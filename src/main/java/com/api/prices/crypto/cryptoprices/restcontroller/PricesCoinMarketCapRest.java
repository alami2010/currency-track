package com.api.prices.crypto.cryptoprices.restcontroller;

import com.api.prices.crypto.cryptoprices.service.AnalyseService;
import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class PricesCoinMarketCapRest {

    @Autowired
    private PriceService priceService;
    @Autowired
    private AnalyseService analyseService;


    @GetMapping(value = "alert")
    public ResponseEntity<?> getPrices() {
        analyseService.alterCurrentCurrency();
        return null;
    }

    @PostMapping(value = "monitoring")
    public ResponseEntity<?> enableAlertPrices() {
        analyseService.initMonitoringOfStats();
        return  null;
    }
}
