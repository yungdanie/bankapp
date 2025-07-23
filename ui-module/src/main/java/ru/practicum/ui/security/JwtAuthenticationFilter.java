package ru.practicum.ui.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.practicum.ui.service.JwtService;

import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    // Для упрощения допустим, что у нас тоже есть публичный токен и мы его валидируем не ходя в сторонний сервис
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getToken(exchange);

        if (token != null) {
            return Mono.fromCallable(() -> jwtService.validateToken(token))
                    .flatMap(auth ->
                            chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
            );
        }

        return chain.filter(exchange);
    }

    private String getToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getCookies().getFirst("JWT"))
                .map(HttpCookie::getValue)
                .orElse(null);
    }
}
