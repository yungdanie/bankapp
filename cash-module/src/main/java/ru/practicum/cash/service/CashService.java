package ru.practicum.cash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.BalanceTransfer;

import java.util.List;

@Service
public class CashService {

    private final WebClient accountsAPI;

    private final WebClient blockerAPI;

    @Autowired
    public CashService(
            @Qualifier("accountsAPI") WebClient accountsAPI,
            @Qualifier("blockerAPI") WebClient blockerAPI
    ) {
        this.accountsAPI = accountsAPI;
        this.blockerAPI = blockerAPI;
    }

    public Mono<Void> deposit(BalanceTransfer balanceTransfer) {
        return blockerAPI.get().uri("/check")
                .retrieve()
                .toBodilessEntity()
                .then(
                        Mono.defer(
                                () ->
                                        accountsAPI.post().uri("/transfer")
                                                .bodyValue(List.of(balanceTransfer))
                                                .retrieve()
                                                .toBodilessEntity()
                        )
                )
                .then();
    }

    public Mono<Void> withdraw(BalanceTransfer balanceTransfer) {
        return blockerAPI.get().uri("/check")
                .retrieve()
                .toBodilessEntity()
                .then()
                .then(
                        Mono.defer(
                                () -> accountsAPI.post().uri("/transfer")
                                        .bodyValue(List.of(new BalanceTransfer(balanceTransfer.login(), balanceTransfer.currencyCode(), balanceTransfer.amount().negate())))
                                        .retrieve()
                                        .toBodilessEntity()))
                .then();
    }
}
