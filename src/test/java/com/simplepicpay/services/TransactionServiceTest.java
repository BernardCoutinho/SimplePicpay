package com.simplepicpay.services;

import com.simplepicpay.domain.user.User;
import com.simplepicpay.domain.user.UserType;
import com.simplepicpay.dtos.TransactionDTO;
import com.simplepicpay.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    private void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AthorizationService authorizeService;

    @Test
    @DisplayName("Should create transaction successfully when all validations passed")
    void createTransactionSuccessfully() throws Exception {
        User sender = new User(1L, "Carlos", "Magno", "11111111100", "carlosmagno@gmail.com", "11111", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Pedro", "Souza", "11111111101", "pedrosouza@gmail.com", "11111", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizeService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotificatiom(sender, "Transaction completed successfully");
        verify(notificationService, times(1)).sendNotificatiom(receiver, "Transaction completed successfully");

    }

    @Test
    @DisplayName("Should throw Exception when transaction when isn't allowed")
    void createTransactionFailed() {

    }
}