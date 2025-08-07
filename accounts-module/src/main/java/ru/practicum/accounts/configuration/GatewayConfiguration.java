package ru.practicum.accounts.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

@Configuration
public class GatewayConfiguration {

    private final String notificationURL;

    private final OAuth2AuthorizedClientManager clientManager;

    public GatewayConfiguration(
            @Value("${gateway.notificationServiceURL}") String notificationServiceURL,
            OAuth2AuthorizedClientManager clientManager
    ) {
        this.notificationURL = notificationServiceURL;
        this.clientManager = clientManager;
    }

    public String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("accounts-service")
                .principal("accounts-service")
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);

        if (client == null) {
            throw new IllegalStateException("Не удалось получить access token");
        }

        return client.getAccessToken().getTokenValue();
    }

    @Bean
    public RestClient notificationAPI() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    String token = getAccessToken();
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                })
                .baseUrl(notificationURL)
                .build();
    }
}
