package com.prorental.carrental.security;


import com.prorental.carrental.domain.User;
import com.prorental.carrental.repository.UserRepository;
import com.prorental.carrental.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//Incoming token comes here first
//Here we strip the token from the header.
//It comes in the header.
public class AuthTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private UserRepository userRepository;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = parseJwt(request);

        try {
            if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {
                Long id = jwtUtils.getIdFromJwtToken(jwtToken);
                request.setAttribute("id", id);
                Optional<User> user = userRepository.findById(id);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.get().getEmail());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            logger.error("User Not Found ${}", e.getMessage());
        }
        //If there are more filters. below code will send this request to the other filters.
        // This is like next in npm next
        filterChain.doFilter(request, response);
    }


    //Register and login examples of methods we don't want to filter

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //compares if the entered string matches my string
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return antPathMatcher.match("/register", request.getServletPath()) ||
                antPathMatcher.match("/login", request.getServletPath());
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(("Authorization"));

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            return null;
        }
    }


}
