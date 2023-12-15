package com.prorental.carrental.dto;

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

    @Size(max = 15)
    @NotNull(message = "Please enter your first name")
    private String firstName;

    @Size(max = 15)
    @NotNull(message = "Please enter your last name")
    private String lastName;

    //We have nothing to do with password.
    @JsonIgnore
    private String password;

//    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
//            message = "Please enter valid phone number")
    @Size(min = 10, max= 14, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    private String phoneNumber;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 150)
    @NotNull(message = "Please enter your email")
    private String email;

    @Size(max = 250)
    @NotNull(message = "Please enter your address")
    private String address;

    @Size(max = 15)
    @NotNull(message = "Please enter your zip code")
    private String zipCode;

    private Set<String> roles;

    private Boolean builtIn;



    public UserDTO(String firstName, String lastName, String phoneNumber, String email, String address, String zipCode,
                        Set<String> roles, Boolean builtIn){
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
    public void setRoles(Set<Role> roles) {
        Set<String> roles1 = new HashSet<>();
        Role[] role = roles.toArray(new Role[roles.size()]);

        for (int i = 0; i < roles.size(); i++) {
            if (role[i].getName().equals(UserRole.ROLE_ADMIN))
                roles1.add("Administrator");
            else
                roles1.add("Customer");
        }

        this.roles = roles1;
    }

}
