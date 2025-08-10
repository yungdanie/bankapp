package ru.practicum.transfer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.commonweb.factory.WebClientFactory;

@Configuration
public class GatewayConfiguration {

    private final String accountsServiceURL;

    private final String exchangeServiceURL;

    private final String blockerServiceURL;

    private final WebClientFactory webClientFactory;

    private static final String CLIENT_REGISTRATION_ID = "transfer-service";

    public GatewayConfiguration(
            @Value("${gateway.accountServiceURL}") String accountsServiceURL,
            @Value("${gateway.exchangeServiceURL}") String exchangeServiceURL,
            @Value("${gateway.blockerServiceURL}") String blockerServiceURL,
            WebClientFactory webClientFactory
    ) {
        this.webClientFactory = webClientFactory;
        this.accountsServiceURL = accountsServiceURL;
        this.blockerServiceURL = blockerServiceURL;
        this.exchangeServiceURL = exchangeServiceURL;
    }

    @Bean
    public WebClient accountsAPI() {
        return webClientFactory.getOAuth2WebClient(accountsServiceURL, CLIENT_REGISTRATION_ID);
    }

    @Bean
    public WebClient blockerAPI() {
        return webClientFactory.getOAuth2WebClient(blockerServiceURL, CLIENT_REGISTRATION_ID);
    }

    @Bean
    public WebClient exchangeAPI() {
        return webClientFactory.getOAuth2WebClient(exchangeServiceURL, CLIENT_REGISTRATION_ID);
    }
}
