package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class NotificationService {

    private static Logger logger  = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    SimpleMailMessage simpleMailMessage;

    @Autowired
    JavaMailSender javaMailSender;


    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETED_TOPIC}, groupId = "grp123")
    public void sendNotification(String msg) throws ParseException {

        logger.info("before sending email - message {}", msg);

        JSONObject data = (JSONObject) new JSONParser().parse(msg);
        String email = (String) data.get("email");
        String emailMsg = (String) data.get("msg");

        simpleMailMessage.setFrom("e.wallet.jbdl33@outlook.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(emailMsg);
        simpleMailMessage.setSubject("E wallet Payment Updates");

        javaMailSender.send(simpleMailMessage);

    }

//    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETED_TOPIC}, groupId = "grp123")
//    public void sendNotification(String msg) throws ParseException {
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", true);
//        props.put("mail.smtp.starttls.enable", true);
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
////        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        props.put("mail.smtp.ssl.trust", "*");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("e.wallet.jbdl33@gmail.com", "eWallet123");
//                    }
//                }
//        );
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("e.wallet.jbdl33@gmail.com"));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse("carolijime@hotmail.com"));
//            message.setSubject("Testing Subject");
//            message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");
//            Transport.send(message);
//            System.out.println("Done");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
