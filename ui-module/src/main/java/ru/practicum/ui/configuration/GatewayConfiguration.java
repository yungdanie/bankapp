package ru.practicum.ui.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.exception.BadRequestException;

@Configuration
public class GatewayConfiguration {

    private final String accountsServiceURL;

    private final String cashServiceURL;

    private final String transferServiceURL;

    public GatewayConfiguration(
            @Value("${gateway.url}") String url,
            @Value("${gateway.accountServiceURL}") String accountsServiceURL,
            @Value("${gateway.cashServiceURL}") String cashServiceURL,
            @Value("${gateway.transferServiceURL}") String transferServiceURL
    ) {
        this.accountsServiceURL = url.concat(accountsServiceURL);
        this.cashServiceURL = url.concat(cashServiceURL);
        this.transferServiceURL = url.concat(transferServiceURL);
    }

    @NotNull
    private WebClient getWebClient(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager, String serviceURL) {
        ExchangeFilterFunction errorHandler = ExchangeFilterFunction.ofResponseProcessor(
                response -> {
                    if (response.statusCode().is4xxClientError()) {
                        return response.bodyToMono(Error.class)
                                .flatMap(body -> Mono.error(new BadRequestException(body.getMessage())));
                    } else {
                        return Mono.just(response);
                    }
                }
        );

        return WebClient.builder()
                .filter((request, next) ->
                        reactiveOAuth2AuthorizedClientManager.authorize(
                                        OAuth2AuthorizeRequest
                                                .withClientRegistrationId("bankui-service")
                                                .principal("bankui")
                                                .build()
                                )
                                .flatMap(authorizedClient -> {
                                    ClientRequest newRequest = ClientRequest.from(request)
                                            .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                                            .build();
                                    return next.exchange(newRequest);
                                })
                )
                .filter(errorHandler)
                .baseUrl(serviceURL)
                .build();
    }

    @Bean
    @LoadBalanced
    public WebClient accountsAPI(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        return getWebClient(reactiveOAuth2AuthorizedClientManager, accountsServiceURL);
    }

    @Bean
    @LoadBalanced
    public WebClient cashAPI(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        return getWebClient(reactiveOAuth2AuthorizedClientManager, cashServiceURL);
    }

    @Bean
    @LoadBalanced
    public WebClient transferAPI(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        return getWebClient(reactiveOAuth2AuthorizedClientManager, transferServiceURL);
    }
}
