package com.prorental.carrental.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prorental.carrental.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1;
    private Long id;
    private String email;

    @JsonIgnore
    private String password;

    //GrandtedAuthority is for keeping roles
    private Collection<? extends GrantedAuthority> authorities;


    //We are writing this build to get the user from userRepo and return a userDetail in UserDetailsServiceImpl
    //Yes we use this build method in UserDetailsServiceImpl line 37. We get the user entity from database.
    //Then we pass it to UserDetailsImpl class to build the User.
    public static UserDetailsImpl build(User user){
       List<GrantedAuthority> authorities = user.getRole().stream().
               map(role->new SimpleGrantedAuthority(role.getName().name())).
               collect(Collectors.toList());
            return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities);
    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

//The below code is old but I want to keep it here.
//  @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    //We are writing this build to get the user from userRepo and return a userDetail in UserDetailsServiceImpl
//    public static UserDetailsImpl build(User user){
//       List<SimpleGrantedAuthority> authorities = user.getRole().stream().
//               map(role->new SimpleGrantedAuthority(role.getName().name())).
//               collect(Collectors.toList());
//            return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities);
//    };
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }