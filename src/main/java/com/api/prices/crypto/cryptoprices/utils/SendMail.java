package com.api.prices.crypto.cryptoprices.utils;

import com.api.prices.crypto.cryptoprices.entity.CurrencyToTrack;
import com.api.prices.crypto.cryptoprices.entity.Decision;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendMail {


    public void sendMail(String subject, String body) {
        try {

            Message message = confMail();

            setToAndFromForMail(message);


            message.setText(body);
            message.setSubject(subject);

            Transport.send(message);
              System.out.println("Sending mail completed!!!");
         } catch (MessagingException e) {
              throw new RuntimeException(e);
        }
  }

    private static void setToAndFromForMail(Message message) throws MessagingException {
        message.setFrom(new InternetAddress("youssef.dahar@gmail.com"));

        // Address[] toUser = InternetAddress.parse("youssef.dahar@gmail.com,admi.mohamad@gmail.com");
        Address[] toUser = InternetAddress.parse("youssef.dahar@gmail.com");

        message.setRecipients(Message.RecipientType.TO, toUser);
    }

    private static MimeMessage confMail() {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("youssef.dahar@gmail.com", "yDahar+1992");
                    }
                });

        session.setDebug(true);
        return new MimeMessage(session);
    }


}
