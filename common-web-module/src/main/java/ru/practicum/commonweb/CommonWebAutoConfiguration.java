package ru.practicum.commonweb;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import ru.practicum.commonweb.controller.ExceptionController;
import ru.practicum.commonweb.factory.RestClientFactory;
import ru.practicum.commonweb.factory.WebClientFactory;

@AutoConfiguration
public class CommonWebAutoConfiguration {

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletHttpConfig {

        @Bean
        @ConditionalOnMissingBean(RestClientFactory.class)
        @ConditionalOnSingleCandidate(OAuth2AuthorizedClientManager.class)
        public RestClientFactory restClientFactory(OAuth2AuthorizedClientManager authorizedClientManager) {
            return new RestClientFactory(authorizedClientManager);
        }

    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class ReactiveConfig {

        @Bean
        @ConditionalOnMissingBean(WebClientFactory.class)
        @ConditionalOnSingleCandidate(ReactiveOAuth2AuthorizedClientManager.class)
        public WebClientFactory restClientFactory(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
            return new WebClientFactory(reactiveOAuth2AuthorizedClientManager);
        }

    }

    @Bean
    @ConditionalOnMissingBean(ExceptionController.class)
    @ConditionalOnProperty(
            name = "web.exception-handler.enabled",
            matchIfMissing = true,
            havingValue = "true"
    )
    public ExceptionController exceptionController() {
        return new ExceptionController();
    }

}
