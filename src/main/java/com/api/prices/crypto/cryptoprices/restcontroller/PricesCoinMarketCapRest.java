package com.api.prices.crypto.cryptoprices.restcontroller;

import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class PricesCoinMarketCapRest {

    @Autowired
    private PriceService priceService;

    @GetMapping(value = "alert")
    public ResponseEntity<?> getPrices(@PathVariable String id) {
        priceService.alterCurrentCurrency();
        return null;
    }

    @PostMapping(value = "alertx/{id}")
    public ResponseEntity<?> enableAlertPrices(@PathVariable String id, @RequestParam Double valueAlert, @RequestParam Boolean enableDisable) {
        //return ResponseEntity.ok(priceService.enableDisableAlert(id, 20.00, enableDisable));
        return null;
    }
}
