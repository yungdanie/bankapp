package ru.practicum.transfer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.Exchange;

import java.math.BigDecimal;

@Service
public class ExchangeService {

    private final WebClient exchangeAPI;

    public ExchangeService(@Qualifier("exchangeAPI") WebClient exchangeAPI) {
        this.exchangeAPI = exchangeAPI;
    }

    public Mono<BigDecimal> exchange(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode) {
        return exchangeAPI.post()
                .uri("/exchange")
                .bodyValue(new Exchange(amount, fromCurrencyCode, toCurrencyCode))
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }
}
