package com.prorental.carrental.security.jwt;


import com.prorental.carrental.repository.UserRepository;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//Incoming token comes here first
//Here we strip the token from the header.
//It comes in the header.
public class AuthTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    //In order to use Autowired, We need to put this into the application context.
    //During run time, component scan finds it and makes an object out of it and then throws it into application context.
    @Autowired//this means application context give me this class. So JwtUtils class must have an annotation like @Service, @controller...
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
            if(jwtToken != null && jwtUtils.validateToken(jwtToken)){
                Long id = jwtUtils.getIdFromJwtToken(jwtToken);
                //When the request comes in we set the id attribute to the request to use it in userController.
                request.setAttribute("id",id);
                Optional<User> user = userRepository.findById(id);
                //This user is found in UserDetailsServiceImpl class and build to a UserDetailsImpl
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.get().getEmail());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            logger.error("Cannot set user authentication ${}", e.getMessage());
        }
        //If there are more filters. below code will send this request to the other filters.
        // This is like next in npm next
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher =new AntPathMatcher();
        //If the request is register or login, do not filter.
        return antPathMatcher.match("/register", request.getServletPath()) ||
                antPathMatcher.match("login", request.getServletPath());
    }

    private String parseJwt(HttpServletRequest request){
        //Bearer 123asdasdas213123123qwdasd
        //get the header with the Authorization from request
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }else{
            return null;
        }
    }


}
