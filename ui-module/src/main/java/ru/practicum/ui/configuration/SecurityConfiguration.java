package ru.practicum.ui.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.practicum.ui.security.JwtAuthenticationFilter;
import ru.practicum.ui.security.LoginSuccessHandler;
import ru.practicum.ui.security.LogoutSuccessHandler;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter,
            LogoutSuccessHandler logoutSuccessHandler,
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            LoginSuccessHandler loginSuccessHandler

    ) {
        return http
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(
                        exchanges -> exchanges
                                .pathMatchers("/").permitAll()
                                .pathMatchers("/signup").permitAll()
                                .pathMatchers("/actuator/health", "/actuator/ready").permitAll()
                                .pathMatchers("/logout").authenticated()
                                .anyExchange().authenticated()
                )
                .logout(logoutSpec ->
                        logoutSpec
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(logoutSuccessHandler)
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .addFilterAt(loginFilter(reactiveAuthenticationManager, loginSuccessHandler), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(spec -> spec.authenticationEntryPoint(authenticationEntryPoint()))
                .build();
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public SecretKey key(@Value("${jwt.secret}") String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public AuthenticationWebFilter loginFilter(ReactiveAuthenticationManager reactiveAuthenticationManager, LoginSuccessHandler loginSuccessHandler) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager) {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                if (exchange.getRequest().getPath().toString().matches("/(login|signup)")) {
                    return chain.filter(exchange); // Пропускаем
                }
                return super.filter(exchange, chain);
            }
        };
        filter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return new RedirectServerAuthenticationEntryPoint("/signup");
    }

    @Bean
    public ServerAuthenticationFailureHandler loginFailureHandler() {
        return (webFilterExchange, exception) -> {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(response.bufferFactory()
                    .wrap("Authentication failed".getBytes())));
        };
    }

}
