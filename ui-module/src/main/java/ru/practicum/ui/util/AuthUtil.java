package ru.practicum.ui.util;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;
import java.util.Objects;

public class AuthUtil {

    protected AuthUtil() {}

    public static void setAuthenticationCookie(ServerWebExchange serverWebExchange, Authentication authentication) {
        String jwt = (String) authentication.getCredentials();

        Objects.requireNonNull(jwt);

        ResponseCookie cookie = ResponseCookie.from("JWT", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        serverWebExchange.getResponse().addCookie(cookie);
    }
}
