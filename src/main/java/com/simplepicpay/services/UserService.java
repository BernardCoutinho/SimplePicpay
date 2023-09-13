package com.simplepicpay.services;

import com.simplepicpay.domain.user.User;
import com.simplepicpay.domain.user.UserType;
import com.simplepicpay.dtos.UserDTO;
import com.simplepicpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws  Exception{
        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("User with type 'merchant' cannot perform a transaction.");
        }

        if(sender.getBalance().compareTo(amount) < 0 ){
            throw new Exception("Insufficient funds.");
        }
    }

    public User findUserById(Long id) throws  Exception{
       return this.repository.findUserById(id).orElseThrow(() -> new Exception("User not found."));
    }

    public User findUserByDocument(String document) throws  Exception{
        return this.repository.findUserByDocument(document).orElseThrow(() -> new Exception("User not found."));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public User createUser(UserDTO userDTO){
        User user = new User(userDTO);
        this.saveUser(user);
        return user;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
}
