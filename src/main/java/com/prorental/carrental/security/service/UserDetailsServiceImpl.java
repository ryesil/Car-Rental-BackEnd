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

@Service
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;


//Bu kullanici eger veri tabaninda varsa load et. Ve userDetail tipinde disari gonder.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //We made a findByEmail method in userRepo and implemented it here.
        User user = userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not found with email "+ email));
        return UserDetailsImpl.build(user);
    };
}
