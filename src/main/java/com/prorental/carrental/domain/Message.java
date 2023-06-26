package com.prorental.carrental.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Not blank needed")
    @NotEmpty(message = "Not empty needed")
    @NotNull(message = "Please provide your name")
    @Size(min=5,max=15, message = "Your subject '${validatedValue}' must be between {min} and {max} characters long")
    @Column( length = 200, nullable = false)
    private String subject;

    @NotBlank(message = "Not blank needed")
    @NotEmpty(message = "Not empty needed")
    @NotNull(message = "Please provide your name")
    @Size(min=5,max=15, message = "Your body '${validatedValue}' must be between {min} and {max} characters long")
    @Column( length = 200, nullable = false)
    private String body;

    @NotBlank(message = "Not blank needed")
    @NotEmpty(message = "Not empty needed")
    @NotNull(message = "Please provide your name")
    @Size(min=5,max=15, message = "Your email '${validatedValue}' must be between {min} and {max} characters long")
    @Column( length = 200, nullable = false)
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Not blank needed")
    @NotEmpty(message = "Not empty needed")
    @NotNull(message = "Please provide your name")
    @Size(min=5,max=14, message = "Your email '${validatedValue}' must be between {min} and {max} characters long")
    @Column( length = 14, nullable = false)
    @Pattern(regexp = "^(?:\\+1)?[ -]?\\(?([2-9][0-8][0-9])\\)?[ -]?([2-9][0-9]{2})[ -]?([0-9]{4})$")
    private String phoneNumber;

}
