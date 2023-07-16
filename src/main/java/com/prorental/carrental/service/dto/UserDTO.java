package com.prorental.carrental.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prorental.carrental.domain.Role;
import com.prorental.carrental.enumaration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//This is not a database table or entity class. so we don't need @Column
public class UserDTO {

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


    //We have nothing to do with password. So it doesn't have to be notNUll.
    @JsonIgnore
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


    private Set<Role> roles = new HashSet<>();


//    public Set<Role> getRole(){
//        return roles;
//    }


    public UserDTO(String firstName, String lastName, String phoneNumber, String email, String address, String zipCode,
                        Set<Role> roles, Boolean builtIn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.roles = roles;
        this.builtIn = builtIn;
    }
    //We changed Roles like Role_admin, Role_customer to administrator and customer to send out.
    public Set<String> getRoles(){
        Set<String> roleStr = new HashSet<>();
        Role[] role = roles.toArray(new Role[roles.size()]);
        for(int i = 0;i < roles.size(); i++){
            if(role[i].getName().equals(UserRole.ROLE_ADMIN)){
                roleStr.add("Administrator");
            } else {
                roleStr.add("Customer");
            }
        }
        return roleStr;
    }
}
