package ru.practicum.common.dto;

import java.math.BigDecimal;

public record BalanceTransfer(String login, String currencyCode, BigDecimal amount) {}
