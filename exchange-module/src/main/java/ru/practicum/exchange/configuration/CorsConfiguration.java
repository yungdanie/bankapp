package ru.practicum.exchange.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors")
public record CorsConfiguration(String origin, String method, String header) {}
