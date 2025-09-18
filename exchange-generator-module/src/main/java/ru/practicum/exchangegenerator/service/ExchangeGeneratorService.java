package ru.practicum.exchangegenerator.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.debug("Generate new rates");

        Set<ExchangeRateUpdate> exchangeRates = Arrays.stream(Currency.values())
                .filter(currency -> !currency.getCode().equals(baseCurrencyCode))
                .map(currency ->
                        new ExchangeRateUpdate(
                                baseCurrencyCode,
                                currency.getCode(),
                                getRandomExchangeRate()
                        )
                ).collect(Collectors.toSet());

        log.debug("Generated new rates: {}", exchangeRates);

        sendNewExchangeRates(exchangeRates);
    }

    private void sendNewExchangeRates(Set<ExchangeRateUpdate> exchangeRates) {
        log.info("Send new exchangeRates");
        exchangeRates.forEach(rate -> kafkaTemplate.send(KafkaTopic.EXCHANGES.name(), rate.toCurrencyCode(), rate));
    }
}
