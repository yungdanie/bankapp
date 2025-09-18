package ru.practicum.accounts.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                auth -> auth.requestMatchers("/api/login", "/api/signup", "/actuator/health/liveness", "/actuator/health/readiness")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                        )
                        .formLogin(AbstractHttpConfigurer::disable)
                        .logout(AbstractHttpConfigurer::disable)
                        .oauth2Login(AbstractHttpConfigurer::disable)
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .build();
    }

    @Bean
    public SecretKey key(@Value("${jwt.secret}") String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public OAuth2AuthorizedClientManager clientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService
    ) {

        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
