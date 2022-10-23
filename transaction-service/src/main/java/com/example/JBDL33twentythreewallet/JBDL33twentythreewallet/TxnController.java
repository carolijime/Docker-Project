package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TxnController {

    @Autowired
    TxnService txnService;

    /**
     * SenderId
     * ReceiverId
     * Reason
     * @return
     */

    @PostMapping("/txn")
    public String initiateTxn(@RequestParam("receiver") String receiver,
                              @RequestParam("purpose") String purpose,
                              @RequestParam("amount") Double amount) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //get user info from context
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return txnService.initiateTxn(user.getUsername(), receiver, purpose, amount);

    }
}
