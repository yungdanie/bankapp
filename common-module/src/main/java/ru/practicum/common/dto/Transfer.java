package ru.practicum.common.dto;

import java.math.BigDecimal;

public record Transfer(
        String fromLogin,
        String toLogin,
        String fromCurrencyCode,
        String toCurrencyCode,
        BigDecimal amount
) {}
