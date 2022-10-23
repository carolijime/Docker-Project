package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class NotificationConfig {

    Properties getCProperties(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.175.211:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        logger.info("Inside wallet getCProperties");
        return properties;
    }

    @Bean
    ConsumerFactory getConsumerFactory(){
//        logger.info("Inside wallet getConsumerFactory");
        return new DefaultKafkaConsumerFactory(getCProperties());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory getListener(){
//        logger.info("Inside wallet getListener");
        ConcurrentKafkaListenerContainerFactory listener = new ConcurrentKafkaListenerContainerFactory();
        listener.setConsumerFactory(getConsumerFactory());
        return listener;
    }

    @Bean
    SimpleMailMessage getMailMessage(){
        return new SimpleMailMessage();
    }

    @Bean
    JavaMailSender getMailSender(){
        //configure mail sender, existing created email account
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com"); //protocol
        javaMailSender.setPort(587);
        javaMailSender.setUsername("e.wallet.jbdl33@gmail.com");
        javaMailSender.setPassword("******");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true); //mandatory configuration
        properties.put("mail.debug", true); //just for debugging purpose

        return javaMailSender;
    }
}
