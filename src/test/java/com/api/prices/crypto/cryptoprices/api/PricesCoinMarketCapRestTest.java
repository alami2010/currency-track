package com.api.prices.crypto.cryptoprices.api;

import com.api.prices.crypto.cryptoprices.restcontroller.PricesCoinMarketCapRest;
import com.api.prices.crypto.cryptoprices.service.PriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PricesCoinMarketCapRest.class)
@RunWith(SpringRunner.class)
public class PricesCoinMarketCapRestTest {

    final String BASE_PATH = "/query/prices/crypto";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PriceService priceService;

    @Test
    public void testGetPrices() throws Exception {
        //TODO
    }

}
