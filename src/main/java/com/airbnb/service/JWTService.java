package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry.duration}")
    private int expiryTime;
    private Algorithm algorithm;

    private final static String USER_NAME="username";
    @PostConstruct
    public void postConstruct(){
        algorithm=Algorithm.HMAC256(algorithmKey);
        /**
         * Algorithm is Static Variable. So, access using class name.
         */
    }

    public String generateToken(PropertyUser propertyUser){
        return JWT.create().
                withClaim(USER_NAME, propertyUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUserName(String token){
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        /**
         * It takes the token and applies algorithm and secrete key to decrypt and then checks issuer and verify token will checks the
         * expiry time
         */
        return decodedJWT.getClaim(USER_NAME).asString();
    }
}
