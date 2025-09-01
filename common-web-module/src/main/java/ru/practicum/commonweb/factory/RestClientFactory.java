package ru.practicum.commonweb.factory;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

@AllArgsConstructor
public class RestClientFactory {

    private final OAuth2AuthorizedClientManager clientManager;

    private String getAccessToken(String clientRegistrationID) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistrationID)
                .principal(clientRegistrationID)
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);

        if (client == null) {
            throw new IllegalStateException("Не удалось получить access token");
        }

        return client.getAccessToken().getTokenValue();
    }

    public RestClient getOAuth2RestClient(String baseURL, String clientRegistrationID) {
        return RestClient.builder()
                .baseUrl(baseURL)
                .requestInterceptor((request, body, execution) -> {
                    String token = getAccessToken(clientRegistrationID);
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                })
                .build();
    }

}
