package ru.practicum.ui.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.ui.util.AuthUtil;

@Component
public class LoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        AuthUtil.setAuthenticationCookie(webFilterExchange.getExchange(), authentication);
        return Mono.fromRunnable(() -> {});
    }
}
