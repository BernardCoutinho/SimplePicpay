package com.simplepicpay.services;

import com.simplepicpay.domain.transaction.Transaction;
import com.simplepicpay.domain.user.User;
import com.simplepicpay.dtos.TransactionDTO;
import com.simplepicpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRepository repository;


    @Autowired
    private AthorizationService authorizeService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws  Exception{
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        this.userService.validateTransaction(sender, transactionDTO.value());

        if(!this.authorizeService.authorizeTransaction(sender, transactionDTO.value())){
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
}
