package com.api.prices.crypto.cryptoprices.service;

import com.api.prices.crypto.cryptoprices.client.pojo.Currency;
import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {


    @Autowired
    private SendMailService sendMail;
    @Autowired
    private HTMLGeneratorService HTMLGeneratorService;
    final String SUBJECT_TO_SEND = "%3s  %1s";
    final String MESSAGE_TO_SEND = "%3s  %1s  price %2s ";

    public void alert(double alertPrice, CurrencyToTrack id, Decision decision) {


        String subject = String.format(SUBJECT_TO_SEND, decision.toString());
        String message = String.format(MESSAGE_TO_SEND, decision.toString(), id.toString(), String.valueOf(alertPrice));

        sendMail.sendMail(subject, message, false);
    }




    public void alert(String title, List<Currency> ...currencies) {


        StringBuffer sb = HTMLGeneratorService.generateHtmlMessage(title,currencies);
        sendMail.sendMail(title, sb.toString(), true);



    }
}
