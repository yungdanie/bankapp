package ru.practicum.cash.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.commonweb.factory.WebClientFactory;

@Configuration
public class GatewayConfiguration {

    private final String accountsServiceURL;

    private final String blockerServiceURL;

    private final WebClientFactory webClientFactory;

    private static final String CLIENT_REGISTRATION_ID = "cash-service";

    public GatewayConfiguration(
            @Value("${gateway.accountServiceURL}") String accountsServiceURL,
            @Value("${gateway.blockerServiceURL}") String blockerServiceURL,
            WebClientFactory webClientFactory
    ) {
        this.accountsServiceURL = accountsServiceURL;
        this.blockerServiceURL = blockerServiceURL;
        this.webClientFactory = webClientFactory;
    }

    @Bean
    public WebClient accountsAPI() {
        return webClientFactory.getOAuth2WebClient(accountsServiceURL, CLIENT_REGISTRATION_ID);
    }

    @Bean
    public WebClient blockerAPI(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        return webClientFactory.getOAuth2WebClient(blockerServiceURL, CLIENT_REGISTRATION_ID);
    }
}
