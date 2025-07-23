package ru.practicum.ui.dto;

import java.math.BigDecimal;

public record CashActionDTO(BigDecimal amount, String currencyCode, String action) {}
