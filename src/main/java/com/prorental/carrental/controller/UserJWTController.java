package com.prorental.carrental.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prorental.carrental.controller.vm.LoginVM;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.security.JwtUtils;
import com.prorental.carrental.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserJWTController {

    //We get the object That we put into the application context by usign autowired
    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user){
     userService.register(user);
     Map<String, Boolean> map=new HashMap<>();
     map.put("User Registered successfully", true);
//     RegisterOk ok = new RegisterOk("Register successful");
//     return new ResponseEntity<>(ok, HttpStatus.CREATED);
     return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    //We add this to the permitAll in webSecurity
    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginVM){
        //We authenticate with email and password using authenticationManager
        //That is why we made VM class.
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVM.getEmail(),loginVM.getPassword()));
       //Since we got the green light (authentication), we put it into the SecurityContextHolder to use it anywhere we want.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //We got the auth so that we can make a token below
        String jwt = jwtUtils.generateToken(authentication);

        //we make a header and put the token into the header.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+jwt);
        //We return a token to the UI.
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    static class JWTToken{
        private String token;
        public JWTToken(String token){
            this.token = token;
        }

        @JsonProperty("jwt_token")
        String getToken(){
            return this.token;
        }

        void setToken(String token){
            this.token = token;
        }

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
