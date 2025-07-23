package ru.practicum.common.dto;

import java.math.BigDecimal;

public record ExchangeRateUpdate(String fromCurrencyCode, String toCurrencyCode, BigDecimal rate) { }