package ru.practicum.exchangegenerator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .cors(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/actuator/health/liveness", "/actuator/health/readiness").permitAll()
                                .anyRequest().authenticated()
                        )
                        .formLogin(AbstractHttpConfigurer::disable)
                        .logout(AbstractHttpConfigurer::disable)
                        .build();
    }
}
