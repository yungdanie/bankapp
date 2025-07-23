package ru.practicum.exchange.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.Exchange;
import ru.practicum.common.dto.ExchangeRateDTO;
import ru.practicum.common.dto.ExchangeRateUpdate;
import ru.practicum.exchange.service.RateService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@AllArgsConstructor
public class ExchangeController {

    private final RateService rateService;

    @PostMapping
    public BigDecimal exchange(@RequestBody Exchange exchange) {
        return rateService.exchange(exchange);
    }

    @GetMapping("/rates")
    public List<ExchangeRateDTO> getRates() {
        return rateService.getRates();
    }

    @PutMapping("/rates")
    public void updateRates(@RequestBody List<ExchangeRateUpdate> rates) {
        rateService.updateRates(rates);
    }
}
