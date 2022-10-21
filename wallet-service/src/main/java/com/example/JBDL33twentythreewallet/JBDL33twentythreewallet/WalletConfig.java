package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@EnableKafka
@Configuration
public class WalletConfig {

//    private static Logger logger = LoggerFactory.getLogger(WalletConfig.class);

    @Bean
    ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    Properties getCProperties(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.164.132:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        logger.info("Inside wallet getCProperties");
        return properties;
    }

    Properties getPProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.164.132:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        logger.info("Inside wallet getPProperties");
        return properties;
    }

    @Bean
    ConsumerFactory getConsumerFactory(){
//        logger.info("Inside wallet getConsumerFactory");
        return new DefaultKafkaConsumerFactory(getCProperties());
    }

    ProducerFactory getProducerFactory(){
//        logger.info("Inside wallet getProducerFactory");
        return new DefaultKafkaProducerFactory(getPProperties());
    }

    @Bean
    KafkaTemplate<String, String> getKafkaTemplate(){
//        logger.info("Inside wallet getKafkaTemplate");
        return new KafkaTemplate(getProducerFactory());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory getListener(){
//        logger.info("Inside wallet getListener");
        ConcurrentKafkaListenerContainerFactory listener = new ConcurrentKafkaListenerContainerFactory();
        listener.setConsumerFactory(getConsumerFactory());
        return listener;
    }

    ////////////////////////////example code below//////////////////////////////

//    private static Logger logger = LoggerFactory.getLogger(WalletConfig.class);
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                "172.17.164.132:9092");
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                "grp123");
//        props.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        props.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        logger.info("Inside wallet ConsumerFactory");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String>
//    kafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        logger.info("Inside wallet ConcurrentKafkaListenerContainerFactory");
//        return factory;
//    }

}