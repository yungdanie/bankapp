package ru.practicum.exchangegenerator.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.common.Currency;
import ru.practicum.common.dto.ExchangeRateDTO;
import ru.practicum.common.dto.ExchangeRateUpdate;
import ru.practicum.exchangegenerator.service.ExchangeGeneratorService;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class Scheduler {

    private final String baseCurrencyCode;

    private final ExchangeGeneratorService exchangeGeneratorService;

    private final RestClient exchangeAPI;

    public Scheduler(
            String baseCurrencyCode,
            ExchangeGeneratorService exchangeGeneratorService,
            @Qualifier("exchangeAPI") RestClient exchangeAPI
    ) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.exchangeGeneratorService = exchangeGeneratorService;
        this.exchangeAPI = exchangeAPI;
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendNewExchangeRates() {
        Set<ExchangeRateUpdate> exchangeRates = Arrays.stream(Currency.values())
                .filter(currency -> !currency.getCode().equals(baseCurrencyCode))
                .map(currency ->
                        new ExchangeRateUpdate(
                                baseCurrencyCode,
                                currency.getCode(),
                                exchangeGeneratorService.getRandomExchangeRate()
                        )
                ).collect(Collectors.toSet());

        sendNewExchangeRates(exchangeRates);
    }

    private void sendNewExchangeRates(Set<ExchangeRateUpdate> exchangeRates) {
        exchangeAPI.put()
                .uri("/exchange/rates")
                .body(exchangeRates)
                .retrieve()
                .toBodilessEntity();
    }
}
