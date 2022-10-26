package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

// separate class from the security config one because of circular dependency

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class UserConfig {

//    private static Logger logger = LoggerFactory.getLogger(UserConfig.class);

    //define password encoder
    @Bean
    PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    ObjectMapper getMapper(){
        return new ObjectMapper();
    }

    Properties getProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //local host of where my kafka server will be running
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); //all messages will be converted into string
//        logger.info("inside user - getProperties");
        return properties;
    }

    ProducerFactory getProducerFactory(){
//        logger.info("inside user - getProducerFactory");
        return new DefaultKafkaProducerFactory(getProperties());
    }

    @Bean
    KafkaTemplate<String, String> getKafkaTemplate(){
//        logger.info("inside user - getKafkaTemplate");
        return new KafkaTemplate(getProducerFactory());
    }

}
