package ru.practicum.exchange.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.Currency;
import ru.practicum.common.dto.Exchange;
import ru.practicum.common.dto.ExchangeRateDTO;
import ru.practicum.common.dto.ExchangeRateUpdate;
import ru.practicum.exchange.model.ExchangeRate;
import ru.practicum.exchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RateService {

    private final ExchangeRateRepository exchangeRateRepository;

    private final String baseCurrencyCode;

    public BigDecimal exchange(Exchange exchange) {
        return getExchangeRate(exchange.fromCurrencyCode(), exchange.toCurrencyCode()).multiply(exchange.amount());
    }

    private BigDecimal getExchangeRate(String fromCurrencyCode, String toCurrencyCode) {
        if (fromCurrencyCode.equals(toCurrencyCode)) {
            return BigDecimal.ONE;
        }

        if (fromCurrencyCode.equals(baseCurrencyCode)) {
            return BigDecimal.ONE.divide(
                    exchangeRateRepository
                            .findByFromCurrencyCodeAndToCurrencyCode(fromCurrencyCode, toCurrencyCode)
                            .getRate(),
                    10,
                    RoundingMode.HALF_UP
            );
        } else if (toCurrencyCode.equals(baseCurrencyCode)) {
            return exchangeRateRepository
                    .findByFromCurrencyCodeAndToCurrencyCode(baseCurrencyCode, fromCurrencyCode).getRate();
        } else {
            var toRubExchange = exchangeRateRepository
                    .findByFromCurrencyCodeAndToCurrencyCode(baseCurrencyCode, fromCurrencyCode);

            var toCurrencyExchange = exchangeRateRepository
                    .findByFromCurrencyCodeAndToCurrencyCode(baseCurrencyCode, toCurrencyCode);

            return toRubExchange.getRate().multiply(toCurrencyExchange.getRate());
        }
    }

    public List<ExchangeRateDTO> getRates() {
        return exchangeRateRepository.findAll().stream()
                .filter(exchangeRate -> !exchangeRate.getToCurrencyCode().equals(baseCurrencyCode))
                .map(exchangeRate -> {
                    var fromCurrency = Arrays.stream(Currency.values())
                            .filter(currency -> currency.getCode().equals(exchangeRate.getFromCurrencyCode()))
                            .findFirst()
                            .orElseThrow();

                    var toCurrency = Arrays.stream(Currency.values())
                            .filter(currency -> currency.getCode().equals(exchangeRate.getToCurrencyCode()))
                            .findFirst()
                            .orElseThrow();

                    return new ExchangeRateDTO(fromCurrency.getName(), toCurrency.getName(), exchangeRate.getRate());
                })
                .toList();
    }

    public void updateRates(ExchangeRateUpdate inputRate) {
        var exchangeRate = exchangeRateRepository.findByFromCurrencyCodeAndToCurrencyCode(
                inputRate.fromCurrencyCode(),
                inputRate.toCurrencyCode()
        );

        if (exchangeRate == null) {
            exchangeRateRepository.save(
                    new ExchangeRate(inputRate.fromCurrencyCode(), inputRate.toCurrencyCode(), inputRate.rate())
            );
        } else {
            exchangeRate.setRate(inputRate.rate());
        }
    }
}
