package ru.practicum.commonweb;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.commonweb.controller.ExceptionController;
import ru.practicum.commonweb.factory.WebClientFactory;

@AutoConfiguration
public class CommonWebAutoConfiguration {

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class ReactiveConfig {

        @Bean
        @ConditionalOnMissingBean(WebClientFactory.class)
        @ConditionalOnSingleCandidate(ReactiveOAuth2AuthorizedClientManager.class)
        public WebClientFactory restClientFactory(
                ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager,
                WebClient.Builder webClientBuilder
        ) {
            return new WebClientFactory(reactiveOAuth2AuthorizedClientManager, webClientBuilder);
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
