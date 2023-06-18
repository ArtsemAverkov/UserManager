package ru.clevertec.UserManager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String secretKey;

    public Jws<Claims> validateToken(String token) {
        try {
//            @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//            String secretKey;// = "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";
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            System.err.println("Error validating JWT signature: " + e.getMessage());
            return null;
        }
    }

}
