package com.prorental.carrental.controller;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.spi.RegisterableService;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserJWTController {


    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user){
     userService.register(user);
     Map<String, Boolean> map=new HashMap<>();
     map.put("User Registered successfully", true);
//     RegisterOk ok = new RegisterOk("Register successful");
//     return new ResponseEntity<>(ok, HttpStatus.CREATED);
     return new ResponseEntity<>(map, HttpStatus.CREATED);
    }


//   static class RegisterOk{
//        private String message;
//        public RegisterOk(String message){
//            this.setMessage(message);
//        }
//        public String getMessage(){
//            return this.message;
//        }
//        public void setMessage(String message){
//            this.message=message;
//        }
//    }
}
