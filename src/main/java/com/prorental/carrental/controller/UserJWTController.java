package com.prorental.carrental.controller;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class UserJWTController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user){
     userService.register(user);
     Map<String, Boolean> map=new HashMap<>();
     map.put("User Registered successfully", true);
     return new ResponseEntity<>(map, HttpStatus.CREATED);
    }



}
