package ru.practicum.ui.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.AccountDTO;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.common.dto.Transfer;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.ui.dto.CashActionDTO;

import java.math.BigDecimal;
import java.util.List;

@Service
@CircuitBreaker(name = "external-service-circuit-breaker", fallbackMethod = "fallbackMethod")
public class AccountService {

    private final WebClient accountsAPI;

    private final WebClient cashAPI;

    private final WebClient transferAPI;

    public AccountService(
            @Qualifier("accountsAPI") WebClient accountsAPI,
            @Qualifier("cashAPI") WebClient cashAPI,
            @Qualifier("transferAPI") WebClient transferAPI
    ) {
        this.accountsAPI = accountsAPI;
        this.cashAPI = cashAPI;
        this.transferAPI = transferAPI;
    }

    public Mono<Void> fallbackMethod(BadRequestException exception) {
        return Mono.error(exception);
    }

    public Mono<Void> editAccounts(List<AccountDTO> accounts) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> accountsAPI.post()
                        .uri(uriBuilder -> uriBuilder.path("/accounts").queryParam("login", login).build())
                        .bodyValue(accounts)
                        .retrieve()
                        .toBodilessEntity()
                        .then()
                );
    }

    public Mono<Void> deposit(CashActionDTO cashActionDTO) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> cashAPI.post()
                        .uri("/deposit")
                        .bodyValue(new BalanceTransfer(login, cashActionDTO.currencyCode(), cashActionDTO.amount()))
                        .retrieve()
                        .toBodilessEntity()
                        .then()
                );
    }

    public Mono<Void> innerTransfer(String toCurrencyCode, String fromCurrencyCode, BigDecimal amount) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> transfer(login, toCurrencyCode, fromCurrencyCode, amount));
    }

    public Mono<Void> transfer(String toLogin, String toCurrencyCode, String fromCurrencyCode, BigDecimal amount) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> transferAPI.post()
                        .uri("/transfer")
                        .bodyValue(
                                new Transfer(
                                        login,
                                        toLogin,
                                        fromCurrencyCode,
                                        toCurrencyCode,
                                        amount
                                )
                        )
                        .retrieve()
                        .toBodilessEntity()
                )
                .then();
    }

    public Mono<Void> withdraw(CashActionDTO cashActionDTO) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> cashAPI.post()
                        .uri("/withdraw")
                        .bodyValue(new BalanceTransfer(login, cashActionDTO.currencyCode(), cashActionDTO.amount()))
                        .retrieve()
                        .toBodilessEntity()
                )
                .then();
    }
}
