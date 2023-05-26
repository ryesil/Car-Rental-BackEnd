package com.prorental.carrental.security.service;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not found with email "+ email));
        return UserDetailsImpl.build(user);
    };
}
