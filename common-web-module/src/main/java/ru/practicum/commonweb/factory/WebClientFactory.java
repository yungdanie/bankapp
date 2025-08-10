package ru.practicum.commonweb.factory;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.exception.BadRequestException;

@AllArgsConstructor
public class WebClientFactory {

    private final ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager;

    public WebClient getOAuth2WebClient(
            String serviceURL,
            String clientRegistrationID
    ) {
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
                                                .withClientRegistrationId(clientRegistrationID)
                                                .principal(clientRegistrationID)
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

}
