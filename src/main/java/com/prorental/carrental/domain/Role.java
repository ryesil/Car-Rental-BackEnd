package com.prorental.carrental.domain;


import com.prorental.carrental.enumaration.UserRole;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Enumerated(EnumType.STRING) //The @Enumerated(EnumType.STRING) annotation specifies that the UserRole enum type should be persisted as a string in the database. This means that when an instance of the UserRole enum is stored in the database, its name (as a string) will be stored in the corresponding column.
    @Column(length = 30, nullable = false)
    private UserRole name;


@Override
    public String toString(){
    return "["+name+"]";
}

}
