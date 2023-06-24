package com.prorental.carrental.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;


import java.io.IOException;

//Yetkilendirilmemis yada giris yapmamis kullanici varsa, onu tesbit edip disari atiyoruz.


public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error ${}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error:Unauthorized");

    }
}
