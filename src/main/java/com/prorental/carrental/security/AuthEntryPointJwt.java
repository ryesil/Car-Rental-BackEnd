package com.prorental.carrental.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;



//This is for the user who is not entitled to go to a page.
//Simply we error out. Or we can direct them to the login page
@Component //So that it can be scanned in the component scan
public class AuthEntryPointJwt  implements AuthenticationEntryPoint {

    private static final Logger Logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    Logger.error("Unauthorized error ${}", authException.getMessage());
    response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error:Unauthorized");
    }
}
