package ru.practicum.ui.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        ServerHttpResponse response = exchange.getExchange().getResponse();

        response.addCookie(
                        ResponseCookie.from("JWT", "")
                                .maxAge(Duration.ZERO)
                                .path("/")
                                .build()
                );

        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("/loign"));

        return response.setComplete();
    }
}
