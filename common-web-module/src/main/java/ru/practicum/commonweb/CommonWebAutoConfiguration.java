package ru.practicum.commonweb;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.commonweb.controller.ExceptionController;
import ru.practicum.commonweb.factory.WebClientFactory;
import ru.practicum.commonweb.metrics.AuthMetrics;
import ru.practicum.commonweb.metrics.FailAuthListener;
import ru.practicum.commonweb.metrics.SuccessAuthListener;

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

    @Bean
    @ConditionalOnMissingBean(AuthMetrics.class)
    @ConditionalOnBean(MeterRegistry.class)
    public AuthMetrics authMetrics(MeterRegistry meterRegistry) {
        return new AuthMetrics(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(FailAuthListener.class)
    @ConditionalOnBean(AuthMetrics.class)
    public FailAuthListener failAuthListener(AuthMetrics authMetrics) {
        return new FailAuthListener(authMetrics);
    }

    @Bean
    @ConditionalOnMissingBean(SuccessAuthListener.class)
    @ConditionalOnBean(AuthMetrics.class)
    public SuccessAuthListener successAuthListener(AuthMetrics authMetrics) {
        return new SuccessAuthListener(authMetrics);
    }
}
