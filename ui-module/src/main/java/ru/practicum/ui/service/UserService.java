package ru.practicum.ui.service;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.*;
import ru.practicum.ui.util.AuthUtil;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final WebClient accountsAPI;

    public UserService(
            @Qualifier("accountsAPI") WebClient accountsAPI
    ) {
        this.accountsAPI = accountsAPI;
    }

    @Retry(name = "signup-retry")
    public Mono<Void> signup(SignupForm form, ServerWebExchange exchange) {
        return accountsAPI
                .post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(tokenResponse -> new UsernamePasswordAuthenticationToken(tokenResponse.login(), tokenResponse.token()))
                .doOnNext(auth -> AuthUtil.setAuthenticationCookie(exchange, auth))
                .then();
    }

    public Mono<Void> editPassword(ChangePasswordForm changePasswordForm) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(login ->
                        accountsAPI
                                .post()
                                .uri("/user/editPassword")
                                .bodyValue(
                                        new ChangePasswordForm(
                                                login,
                                                changePasswordForm.password(),
                                                changePasswordForm.confirmPassword()
                                        )
                                )
                                .retrieve()
                                .toBodilessEntity()
                                .then()
                );
    }

    @Retry(name = "login-retry")
    public Mono<TokenResponse> login(String login, String rawPassword) {
        return accountsAPI
                .post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginForm(login, rawPassword))
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    @Retry(name = "current-user-retry")
    public Mono<UserDTO> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(login -> accountsAPI
                        .get()
                        .uri(uribuilder -> uribuilder
                                .path("/user")
                                .queryParam("login", login)
                                .build()
                        )
                        .retrieve()
                        .bodyToMono(UserDTO.class)
                );
    }

    @Retry(name = "all-users-retry")
    public Mono<List<UserDTO>> getAllUsers() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .flatMap(login -> accountsAPI
                        .get()
                        .uri("/users")
                        .retrieve()
                        .bodyToFlux(UserDTO.class)
                        .collectList()
                );
    }
}
