package ru.practicum.cash.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.cash.service.CashService;
import ru.practicum.common.dto.BalanceTransfer;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping
    @RequestMapping("/deposit")
    public Mono<Void> deposit(@RequestBody BalanceTransfer cash) {
        return cashService.deposit(cash);
    }

    @PostMapping
    @RequestMapping("/withdraw")
    public Mono<Void> withdraw(@RequestBody BalanceTransfer cash) {
        return cashService.withdraw(cash);
    }
}
