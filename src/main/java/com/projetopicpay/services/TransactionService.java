package com.projetopicpay.services;

import com.projetopicpay.domain.transaction.Transaction;
import com.projetopicpay.domain.user.User;
import com.projetopicpay.dtos.TransactionDTO;
import com.projetopicpay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private  UserServices userServices;

    @Autowired
    private TransactionRepository TransactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transaction) throws Exception {

        User sender = this.userServices.findUserById(transaction.senderId());
        User receiver = this.userServices.findUserById(transaction.receiverId());

        userServices.validateTransaction(sender,transaction.value());

        boolean isAutorized = this.authorizeTransaction(sender,transaction.value());
        if(!isAutorized){
            throw new Exception("Transação não autorizada");
        }
        Transaction newtransaction = new Transaction();
        newtransaction.setAmount(transaction.value());
        newtransaction.setSender(sender);
        newtransaction.setReceiver(receiver);
        newtransaction.setTimestamp(LocalDateTime.now());


        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.TransactionRepository.save(newtransaction);
        this.userServices.saveUser(sender);
        this.userServices.saveUser(receiver);
    }
    public boolean authorizeTransaction (User sender, BigDecimal value) {
      ResponseEntity <Map> autorizationResponse = restTemplate.getForEntity(  "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);


      if(autorizationResponse.getStatusCode()== HttpStatus.OK ){
         String message = (String)autorizationResponse.getBody().get("mesage");
          return "Autorizado".equalsIgnoreCase(message);
      } else return false;
    }
}
