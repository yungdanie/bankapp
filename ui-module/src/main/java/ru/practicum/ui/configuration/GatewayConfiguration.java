package ru.practicum.ui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.commonweb.factory.WebClientFactory;

@Configuration
public class GatewayConfiguration {

    private final String accountsServiceURL;

    private final String cashServiceURL;

    private final String transferServiceURL;

    private final WebClientFactory webClientFactory;

    private static final String CLIENT_REGISTRATION_ID = "bankui-service";

    public GatewayConfiguration(
            WebClientFactory webClientFactory,
            @Value("${gateway.accountServiceURL}") String accountsServiceURL,
            @Value("${gateway.cashServiceURL}") String cashServiceURL,
            @Value("${gateway.transferServiceURL}") String transferServiceURL
    ) {
        this.webClientFactory = webClientFactory;
        this.accountsServiceURL = accountsServiceURL;
        this.cashServiceURL = cashServiceURL;
        this.transferServiceURL = transferServiceURL;
    }

    @Bean
    public WebClient accountsAPI() {
        return webClientFactory.getOAuth2WebClient(accountsServiceURL, CLIENT_REGISTRATION_ID);
    }

    @Bean
    public WebClient cashAPI() {
        return webClientFactory.getOAuth2WebClient(cashServiceURL, CLIENT_REGISTRATION_ID);
    }

    @Bean
    public WebClient transferAPI() {
        return webClientFactory.getOAuth2WebClient(transferServiceURL, CLIENT_REGISTRATION_ID);
    }
}
