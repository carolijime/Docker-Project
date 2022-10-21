package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class TxnService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(TxnService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // data from user service and then cache at txn service's end
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBasicAuth("txn_service", "txn123");

        HttpEntity request = new HttpEntity(httpHeaders);

        UserDetails requestedUser = restTemplate.exchange("http://localhost:6000/admin/user/" + username,
                HttpMethod.GET, request, UserDetails.class)
                .getBody();

        return requestedUser;
    }

    public String initiateTxn(String sender, String receiver, String purpose ){
        logger.info("Inside initiateTxn method with sender - {}, receiver - {} , purpose - {}", sender, receiver, purpose);
        return null;
    }
}
