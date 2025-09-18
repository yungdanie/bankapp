package ru.practicum.accounts.service;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.expiration}")
    private Duration expiration;

    private final SecretKey key;

    @Autowired
    public JwtService(SecretKey key) {
        this.key = key;
    }

    public String generateTokenForUser(String login) {
        return generateToken(login, Set.of("USER"));
    }

    private String generateToken(String login, Collection<String> roles) {
        log.debug("Generating token for user: {}", login);
        var now = Instant.now();
        return Jwts.builder()
                .subject(login)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(key)
                .compact();
    }
}
