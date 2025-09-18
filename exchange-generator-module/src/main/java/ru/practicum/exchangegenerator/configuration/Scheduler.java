package ru.practicum.exchangegenerator.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.practicum.exchangegenerator.service.ExchangeGeneratorService;

import java.util.concurrent.TimeUnit;

@Component
public class Scheduler {

    private final ExchangeGeneratorService exchangeGeneratorService;

    public Scheduler(
            ExchangeGeneratorService exchangeGeneratorService
    ) {
        this.exchangeGeneratorService = exchangeGeneratorService;
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendNewExchangeRates() {
        exchangeGeneratorService.generateNewRates();
    }
}
