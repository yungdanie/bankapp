package ru.practicum.ui.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey key;

    @Autowired
    public JwtService(SecretKey key) {
        this.key = key;
    }

    public UsernamePasswordAuthenticationToken validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)  // Ключ для проверки подписи
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            var login = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            return new UsernamePasswordAuthenticationToken(
                    login,
                    token,
                    roles.stream().map(SimpleGrantedAuthority::new).toList()
            );
        } catch (JwtException | IllegalArgumentException e) {
            throw new ValidationException("JWT-невалиден");
        }
    }
}
