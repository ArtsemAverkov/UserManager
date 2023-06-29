package ru.clevertec.UserManager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Component class for parsing and validating JWT tokens.
 */
@Component
public class JwtTokenParser {

    @Autowired
    private static String secretKey;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public void setSecretKey(String secretKey) {
        JwtTokenParser.secretKey = secretKey;
    }

    /**
     * Validates a JWT token and returns the parsed claims.
     * @param token the JWT token to validate
     * @return the parsed claims if the token is valid, null otherwise
     */
    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            System.err.println("Error validating JWT signature: " + e.getMessage());
            return null;
        }
    }

}
