package com.prorental.carrental.security.jwt;

import com.prorental.carrental.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

//Here we generate and validate token, then we also get the user id from the token.
@Component//this will allow spring to manage JwUtils class like a bean.
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${prorent.app.jwtSecret}")// we defined a value in application.yml and use it here under Value annotation
    private String jwtSecret;

    @Value("${prorent.app.jwtExpirationMs}")
    private Long jwtExpirationMs;


    //this is the token we make for the user. We play user's id, issue time and expiration, and signature in it.
    // we can see all but signature at the end.
public String generateJwtToken(Authentication  authentication){
   Instant now = Instant.now();
   Instant expiration = now.plus(Duration.ofMillis(jwtExpirationMs));
   //Principle refers to the currently authenticated user.
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder().
            setSubject(""+(userPrincipal.getId())).
            setIssuedAt(Date.from(now)).
            setExpiration(Date.from(expiration)).
            signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
}

// We need this in the AuthTokenFilter to validate the incoming token
//    Jwts.parser() creates a new JWT parser instance.
//setSigningKey(jwtSecret) configures the parser with the key needed for signature validation.
//parseClaimsJws(token) parses the token and verifies its signature against the provided key.
// If the token's signature is valid, it returns a Jws<Claims> object containing the token's claims.
public boolean validateToken(String token){

    try {
        //after parseClaimsJws we can continue and get body as well but we don't need that here.
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        return true;
    } catch (ExpiredJwtException e) {
        logger.error("JWT Token expired ${}",e.getMessage());
    } catch (UnsupportedJwtException e) {
        logger.error("Unsupported Jwt ${}",e.getMessage());
    } catch (MalformedJwtException e) {
        logger.error("Mal formed Jwt ${}",e.getMessage());
    } catch (SignatureException e) {
        logger.error("Failed Signature ${}",e.getMessage());
    } catch (IllegalArgumentException e) {
        logger.error("JWT Token Illegal ${}",e.getMessage());
    }
 return false;
}

//In the AuthTokenFilter if token gets validated, we get the id using below method.
public Long getIdFromJwtToken(String token){
    return Long.parseLong(Jwts.
            parser().
            setSigningKey(jwtSecret).
            parseClaimsJws(token).
            getBody().
            getSubject());
}



}
