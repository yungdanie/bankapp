package ru.practicum.ui.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.debug("Starting editing accounts: {}", accounts);

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> accountsAPI.post()
                        .uri(uriBuilder -> uriBuilder.path("/accounts").queryParam("login", login).build())
                        .bodyValue(accounts)
                        .retrieve()
                        .toBodilessEntity()
                        .then()
                )
                .doOnError(throwable -> log.error("Error while editing accounts: {}, error: {}", accounts, throwable))
                .doOnSuccess(any -> log.info("Success editing accounts: {}", accounts));
    }

    public Mono<Void> deposit(CashActionDTO cashActionDTO) {
        log.debug("Start deposit: {}", cashActionDTO);

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> cashAPI.post()
                        .uri("/deposit")
                        .bodyValue(new BalanceTransfer(login, cashActionDTO.currencyCode(), cashActionDTO.amount()))
                        .retrieve()
                        .toBodilessEntity()
                        .then()
                )
                .doOnError(throwable -> log.error("Error while deposit: {}, error: {}", cashActionDTO, throwable))
                .doOnSuccess(any -> log.info("Success deposit: {}", cashActionDTO));
    }

    public Mono<Void> innerTransfer(String toCurrencyCode, String fromCurrencyCode, BigDecimal amount) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> transfer(login, toCurrencyCode, fromCurrencyCode, amount));
    }

    public Mono<Void> transfer(String toLogin, String toCurrencyCode, String fromCurrencyCode, BigDecimal amount) {
        log.debug("Start transfer toLogin: {}, toCurrencyCode: {}, fromCurrencyCode: {}, amount: {}", toLogin, toCurrencyCode, fromCurrencyCode, amount);

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
                .then()
                .doOnError(throwable ->
                        log.error(
                                "Error while transfer toLogin: {}, toCurrencyCode: {}, fromCurrencyCode: {}, amount: {}",
                                toLogin,
                                toCurrencyCode,
                                fromCurrencyCode,
                                amount
                        )
                )
                .doOnSuccess(any ->
                        log.info(
                                "Success transfer toLogin: {}, toCurrencyCode: {}, fromCurrencyCode: {}, amount: {}",
                                toLogin,
                                toCurrencyCode,
                                fromCurrencyCode,
                                amount)
                );
    }

    public Mono<Void> withdraw(CashActionDTO cashActionDTO) {
        log.debug("Start withdraw: {}", cashActionDTO);

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(login -> cashAPI.post()
                        .uri("/withdraw")
                        .bodyValue(new BalanceTransfer(login, cashActionDTO.currencyCode(), cashActionDTO.amount()))
                        .retrieve()
                        .toBodilessEntity()
                )
                .then()
                .doOnError(throwable -> log.error("Error while withdraw: {}, error: {}", cashActionDTO, throwable))
                .doOnSuccess(any -> log.info("Success withdraw: {}", cashActionDTO));
    }
}
