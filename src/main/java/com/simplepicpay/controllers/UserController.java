package com.simplepicpay.controllers;


import com.simplepicpay.domain.user.User;
import com.simplepicpay.dtos.UserDTO;
import com.simplepicpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        User user = this.service.createUser(userDTO);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = this.service.getAllUsers();
        return new ResponseEntity(users, HttpStatus.OK);
    }
}
