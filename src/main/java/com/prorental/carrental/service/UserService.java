package com.prorental.carrental.service;

import com.prorental.carrental.domain.Role;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.enumaration.UserRole;
import com.prorental.carrental.exception.BadRequestException;
import com.prorental.carrental.exception.ConflictException;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.RoleRepository;
import com.prorental.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public void register(User user) throws BadRequestException {
    if(userRepository.existsByEmail(user.getEmail())){
        throw new ConflictException("Error: Email is already in use");
    } else {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBuildIn(false);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(UserRole.ROLE_CUSTOMER).orElseThrow(()-> new ResourceNotFoundException("Role Not Found"));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    }


}
