package com.api.prices.crypto.cryptoprices.utils;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendMail {


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

    public void sendMail(String subject, String body, boolean isHtml) {
        try {

            Message message = confMail();

            setToAndFromForMail(message);


            if (isHtml) message.setContent(body, "text/html");
            else message.setText(body);
            message.setSubject(subject);

            Transport.send(message);
            System.out.println("Sending mail completed!!!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
