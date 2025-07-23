package ru.practicum.ui.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.ui.service.JwtService;
import ru.practicum.ui.service.UserService;

@Component
@RequiredArgsConstructor
public class AccountServiceReactiveAuthManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getName() == null || authentication.getCredentials() == null) {
            return Mono.error(new BadCredentialsException("Missing username or password"));
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        return userService.login(username, password)
                .map(tokenResponse -> jwtService.validateToken(tokenResponse.token()));
    }
}
