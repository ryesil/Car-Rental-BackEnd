package com.prorental.carrental.security.service;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Getter
@Setter
//UserDetailsService  loads user-specific data
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

//In most use cases, authentication providers extract user identity information based on
// credentials from a database and then perform validation. Because this use case is so common,
// Spring developers decided to extract it as a separate interface, which exposes the single function:
//loadUserByUsername accepts username as a parameter and returns the user identity object.

//Bu kullanici eger veri tabaninda varsa load et. Ve userDetail tipinde disari gonder.
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //We made a findByEmail method in userRepo and implemented it here.
        User user = userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not found with email "+ email));
        return UserDetailsImpl.build(user);
    };
}
