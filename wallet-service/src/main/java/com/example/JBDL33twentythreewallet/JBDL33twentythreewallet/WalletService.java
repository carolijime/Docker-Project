package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import com.example.JBDL33twentythreewallet.JBDL33twentythreewallet.CommonConstants;
import com.example.JBDL33twentythreewallet.JBDL33twentythreewallet.UserIdentifier;
import com.example.JBDL33twentythreewallet.JBDL33twentythreewallet.Wallet;
import com.example.JBDL33twentythreewallet.JBDL33twentythreewallet.WalletRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private static Logger logger  = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC, groupId = "grp123")
    public void createWallet(String msg) throws ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(msg);

        String phoneNumber = (String)data.get(CommonConstants.USER_CREATION_TOPIC_PHONE_NUMBER);
        Long userId = (Long) data.get(CommonConstants.USER_CREATION_TOPIC_USERID);
        String identifierKey = (String)data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIER_KEY);
        String identifierValue = (String)data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIER_VALUE);

        Wallet wallet = Wallet.builder()
                .userId(userId)
                .phoneNumber(phoneNumber)
                .userIdentifier(UserIdentifier.valueOf(identifierKey))  //use valueOf bacause it is a Enum
                .identifierValue(identifierValue)
                .balance(10.0)
                .build();

        walletRepository.save(wallet);

    }

    @KafkaListener(topics = CommonConstants.TRANSACTION_CREATION_TOPIC, groupId = "grp123" )
    public void updateWalletForTxn(String msg) throws ParseException, JsonProcessingException {

        JSONObject data = (JSONObject) new JSONParser().parse(msg);

        String sender = (String)data.get("sender");
        String receiver = (String)data.get("receiver");
        Double amount = (Double) data.get("amount");
        String txnId = (String) data.get("txnId");

        logger.info("Updating wallets: sender - {}, receiver - {}, amount - {}, txnId - {}", sender, receiver, amount, txnId);

        Wallet senderWallet = walletRepository.findByPhoneNumber(sender);
        Wallet receiverWallet = walletRepository.findByPhoneNumber(receiver);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txnId", txnId);
        jsonObject.put("sender", sender);
        jsonObject.put("receiver", receiver);
        jsonObject.put("amount", amount);


        //check if sender's wallet has enough money (balance) to make the transaction
        if(senderWallet == null || receiverWallet == null || senderWallet.getBalance() < amount){
            jsonObject.put("walletUpdateStatus", WalletUpdateStatus.FAILED);
            kafkaTemplate.send(CommonConstants.WALLET_UPDATED_TOPIC, objectMapper.writeValueAsString(jsonObject));
            return;
        }

        //update the wallets
        walletRepository.updateWallet(receiver, amount); // +10
        walletRepository.updateWallet(sender, 0 - amount); // -10

        // TODO: Kafka for wallet update
        jsonObject.put("walletUpdateStatus", WalletUpdateStatus.SUCCESS);
        kafkaTemplate.send(CommonConstants.WALLET_UPDATED_TOPIC, objectMapper.writeValueAsString(jsonObject));

    }

}