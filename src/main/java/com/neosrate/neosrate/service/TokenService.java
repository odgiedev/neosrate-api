package com.neosrate.neosrate.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.neosrate.neosrate.data.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withClaim("userId", user.getId())
                    .withClaim("username", user.getUsername())
                    .withIssuer("neosrate")
                    .withSubject(user.getEmail())
                    //.withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception ) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("neosrate")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception ) {
            throw new RuntimeException("Error while valid token", exception);
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }
}
