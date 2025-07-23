package ru.practicum.transfer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.common.dto.Transfer;
import ru.practicum.common.exception.ValidateException;

import java.util.List;

@Service
public class TransferService {

    private final WebClient blockerAPI;

    private final AccountService accountService;

    private final ExchangeService exchangeService;

    public TransferService(
            @Qualifier("blockerAPI") WebClient blockerAPI,
            AccountService accountService,
            ExchangeService exchangeService
    ) {
        this.blockerAPI = blockerAPI;
        this.accountService = accountService;
        this.exchangeService = exchangeService;
    }

    public Mono<Void> validateTransfer(Transfer transfer) {
        if (transfer.amount() == null
            || transfer.fromCurrencyCode() == null
            || transfer.toCurrencyCode() == null
            || transfer.fromLogin() == null
            || transfer.toLogin() == null) {
            return Mono.error(new ValidateException("Все поля перевода должны быть заполнены"));
        }

        return Mono.empty();
    }

    public Mono<Void> sameCurrencyTransfer(Transfer transfer) {
        var balanceTransferList = List.of(
                new BalanceTransfer(transfer.fromLogin(), transfer.fromCurrencyCode(), transfer.amount().negate()),
                new BalanceTransfer(transfer.toLogin(), transfer.toCurrencyCode(), transfer.amount())
        );

        return accountService.transfer(balanceTransferList);
    }

    public Mono<Void> differentCurrencyTransfer(Transfer transfer) {
        return exchangeService.exchange(
                transfer.amount(),
                transfer.fromCurrencyCode(),
                transfer.toCurrencyCode()
        ).flatMap(exchangedAmount -> {
            var balanceTransferList = List.of(
                    new BalanceTransfer(transfer.fromLogin(), transfer.fromCurrencyCode(), transfer.amount().negate()),
                    new BalanceTransfer(transfer.toLogin(), transfer.toCurrencyCode(), exchangedAmount)
            );
            return accountService.transfer(balanceTransferList);
        });
    }

    public Mono<Void> transfer(Transfer transfer) {
        return validateTransfer(transfer)
                .then(blockCheck())
                .then(Mono.defer(() -> transfer.toCurrencyCode().equals(transfer.fromCurrencyCode()) ?
                        sameCurrencyTransfer(transfer) : differentCurrencyTransfer(transfer)));
    }

    public Mono<Void> blockCheck() {
        return blockerAPI.get().uri("/check")
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
