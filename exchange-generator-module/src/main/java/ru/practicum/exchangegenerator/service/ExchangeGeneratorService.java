package ru.practicum.exchangegenerator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class ExchangeGeneratorService {

    private final Random random = new Random();

    public BigDecimal getRandomExchangeRate() {
        return BigDecimal.valueOf(random.nextDouble(100));
    }
}
