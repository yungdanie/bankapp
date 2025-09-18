package ru.practicum.exchangegenerator.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.common.Currency;
import ru.practicum.common.KafkaTopic;
import ru.practicum.common.dto.ExchangeRateUpdate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeGeneratorService {

    private final String baseCurrencyCode;

    private final KafkaTemplate<String, ExchangeRateUpdate> kafkaTemplate;

    private final Random random = new Random();

    public ExchangeGeneratorService(String baseCurrencyCode, KafkaTemplate<String, ExchangeRateUpdate> kafkaTemplate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.kafkaTemplate = kafkaTemplate;
    }

    private BigDecimal getRandomExchangeRate() {
        return BigDecimal.valueOf(random.nextDouble(100));
    }

    public void generateNewRates() {
        Set<ExchangeRateUpdate> exchangeRates = Arrays.stream(Currency.values())
                .filter(currency -> !currency.getCode().equals(baseCurrencyCode))
                .map(currency ->
                        new ExchangeRateUpdate(
                                baseCurrencyCode,
                                currency.getCode(),
                                getRandomExchangeRate()
                        )
                ).collect(Collectors.toSet());

        sendNewExchangeRates(exchangeRates);
    }

    private void sendNewExchangeRates(Set<ExchangeRateUpdate> exchangeRates) {
        exchangeRates.forEach(rate -> kafkaTemplate.send(KafkaTopic.EXCHANGES.name(), rate.toCurrencyCode(), rate));
    }
}
