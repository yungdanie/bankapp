package ru.practicum.common.dto;

import java.math.BigDecimal;

public record AccountDTO(BigDecimal balance, String currencyName, String currencyCode, boolean deleted) {}
