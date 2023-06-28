package com.prorental.carrental.controller.vm;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//Since We login with email and password
@Getter
@Setter
public class LoginVM {
    @Email(message = "Please provide a valid Email")
    @NotNull(message = "Please provide your Email")
    @Size(min=5,max=100, message = "Email '${validatedValue}' must be between {min} and {max} characters long")
    private String email;

    @Size(min=4,max=60, message = "Password '${validatedValue}' must be between {min} and {max} characters long")
    @NotBlank(message = "Please provide your password")
    @NotNull(message = "Please provide your password")
    private String password;

    @Override
    public String toString() {
        return "LoginVM{"+"email="+email+"}";
    }
}
