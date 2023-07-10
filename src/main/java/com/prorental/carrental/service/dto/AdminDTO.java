package com.prorental.carrental.service.dto;

import com.prorental.carrental.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.*;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminDTO {
    @NotBlank(message = "FirstName cannot be blank")
    @NotEmpty(message = "FirstName cannot be Empty")
    @NotNull(message = "Please provide your FirstName")
    @Size(min=1,max=15, message = "FirstName '${validatedValue}' must be between {min} and {max} characters long")
    private String firstName;

    @NotBlank(message = "LastName cannot be blank")
    @NotEmpty(message = "LastName cannot be Empty")
    @NotNull(message = "Please provide your LastName")
    @Size(min=1,max=15, message = "LastName '${validatedValue}' must be between {min} and {max} characters long")
    private String lastName;


    @Email(message = "Please provide a valid Email")
    @NotNull(message = "Please provide your Email")
    @Size(min=5,max=100, message = "Email '${validatedValue}' must be between {min} and {max} characters long")
    private String email;


    @Size(min=4,max=60, message = "Password '${validatedValue}' must be between {min} and {max} characters long")
    @NotBlank(message = "Please provide your password")
    @NotNull(message = "Please provide your password")
    private String password;


    @NotBlank(message = "Not blank needed")
    @NotEmpty(message = "Not empty needed")
    @NotNull(message = "Please provide your name")
    @Size(min=5,max=14, message = "Your email '${validatedValue}' must be between {min} and {max} characters long")
    @Pattern(regexp = "^(?:\\+1)?[ -]?\\(?([2-9][0-8][0-9])\\)?[ -]?([2-9][0-9]{2})[ -]?([0-9]{4})$")
    private String phoneNumber;



    @NotBlank(message = "address cannot be blank")
    @NotEmpty(message = "address cannot be Empty")
    @NotNull(message = "Please provide your address")
    @Size(min=10,max=250, message = "Address '${validatedValue}' must be between {min} and {max} characters long")
    private String address;


    @NotBlank(message = "zipCode cannot be blank")
    @NotEmpty(message = "zipCode cannot be Empty")
    @NotNull(message = "Please provide your zipCode")
    @Size(min=4,max=15, message = "zipCode '${validatedValue}' must be between {min} and {max} characters long")
    private String zipCode;


    private Boolean builtIn;

    //So this is incoming data, and it comes as a String we will convert it to Role.
    private Set<String> roles;



}
