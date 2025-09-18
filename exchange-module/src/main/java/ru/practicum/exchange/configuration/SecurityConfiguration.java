package ru.practicum.exchange.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(ru.practicum.exchange.configuration.CorsConfiguration corsConfiguration, HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .cors(cors -> cors.configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.addAllowedOrigin(corsConfiguration.origin());
                            config.addAllowedMethod(corsConfiguration.method());
                            config.addAllowedHeader(corsConfiguration.header());
                            return config;
                        }))
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/exchange/rates", "/actuator/health/liveness", "/actuator/health/readiness").permitAll()
                                .anyRequest().authenticated())
                        .formLogin(AbstractHttpConfigurer::disable)
                        .logout(AbstractHttpConfigurer::disable)
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .oauth2Login(AbstractHttpConfigurer::disable)
                        .build();
    }
}
