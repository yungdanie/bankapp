package ru.practicum.exchangegenerator.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.practicum.common.Currency;

@Component
@AllArgsConstructor
public class CurrencyConfiguration {

    @Bean("baseCurrencyCode")
    public String baseCurrencyCode() {
        return Currency.RUB.getCode();
    }
}
