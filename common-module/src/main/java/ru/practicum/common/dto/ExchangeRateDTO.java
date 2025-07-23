package ru.practicum.common.dto;

import java.math.BigDecimal;

public record ExchangeRateDTO(String fromCurrencyName, String toCurrencyName, BigDecimal rate) {}
