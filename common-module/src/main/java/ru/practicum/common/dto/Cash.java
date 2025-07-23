package ru.practicum.common.dto;

import java.math.BigDecimal;

public record Cash(String login, String currencyCode, BigDecimal amount) {}
