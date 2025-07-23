package ru.practicum.exchangegenerator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

@Configuration
public class GatewayConfiguration {

    private final String exchangeServiceURL;

    private final OAuth2AuthorizedClientManager clientManager;

    public GatewayConfiguration(
            @Value("${gateway.url}") String url,
            @Value("${gateway.exchangeServiceURL}") String exchangeServiceURL,
            OAuth2AuthorizedClientManager clientManager
    ) {
        this.exchangeServiceURL = url.concat(exchangeServiceURL);
        this.clientManager = clientManager;
    }

    public String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("exchange-generator-service")
                .principal("exchange-generator-service")
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);

        if (client == null) {
            throw new IllegalStateException("Не удалось получить access token");
        }

        return client.getAccessToken().getTokenValue();
    }

    @Bean
    @LoadBalanced
    public RestClient exchangeAPI() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    String token = getAccessToken();
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                })
                .baseUrl(exchangeServiceURL)
                .build();
    }
}