package ru.practicum.exchangegenerator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.practicum.commonweb.factory.RestClientFactory;

@Configuration
public class GatewayConfiguration {

    private final String exchangeServiceURL;

    private final RestClientFactory restClientFactory;

    public GatewayConfiguration(
            @Value("${gateway.exchangeServiceURL}") String exchangeServiceURL,
            RestClientFactory restClientFactory
    ) {
        this.exchangeServiceURL = exchangeServiceURL;
        this.restClientFactory = restClientFactory;
    }

    @Bean
    public RestClient exchangeAPI() {
        return restClientFactory.getOAuth2RestClient(exchangeServiceURL, "exchange-generator-service");
    }
}