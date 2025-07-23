package ru.practicum.common.dto;

import java.math.BigDecimal;

public record Exchange(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode) {}
