package com.simplepicpay.services;

import com.simplepicpay.domain.transaction.Transaction;
import com.simplepicpay.domain.user.User;
import com.simplepicpay.dtos.TransactionDTO;
import com.simplepicpay.repositories.TransactionRepository;
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
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRepository repository;


    @Autowired
    private RestTemplate restTemplate;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws  Exception{
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        this.userService.validateTransaction(sender, transactionDTO.value());

        if(!this.authorizeTransaction(sender, transactionDTO.value())){
            throw new Exception("Transaction not allowed.");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimeStamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        this.repository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotificatiom(sender, "Transaction completed successfully");
        this.notificationService.sendNotificatiom(receiver, "Transaction completed successfully");

        return transaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
         ResponseEntity<Map> response = this.restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

         if(response.getStatusCode() == HttpStatus.OK){
             String message = (String) response.getBody().get("message");
             return message.equalsIgnoreCase("Autorizado");
         } else return false;
    }

}
