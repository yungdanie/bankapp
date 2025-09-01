package ru.practicum.accounts.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.practicum.commonweb.factory.RestClientFactory;

@Configuration
public class GatewayConfiguration {

    private final String notificationURL;

    private final RestClientFactory restClientFactory;

    public GatewayConfiguration(
            @Value("${gateway.notificationServiceURL}") String notificationServiceURL,
            RestClientFactory restClientFactory
    ) {
        this.notificationURL = notificationServiceURL;
        this.restClientFactory = restClientFactory;
    }

    @Bean
    public RestClient notificationAPI() {
        return restClientFactory.getOAuth2RestClient(
                notificationURL,
                "accounts-service"
        );
    }
}
