package ru.practicum.blocker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/actuator/health/liveness", "/actuator/health/readiness").permitAll()
                                .anyRequest().authenticated())
                        .formLogin(AbstractHttpConfigurer::disable)
                        .logout(AbstractHttpConfigurer::disable)
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .build();
    }
}
