package ru.practicum.transfer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.transfer.metrics.TransferMetrics;

import java.util.List;

@Service
public class AccountService {

    private final WebClient accountsAPI;

    private final TransferMetrics transferMetrics;

    public AccountService(@Qualifier("accountsAPI") WebClient accountsAPI, TransferMetrics transferMetrics) {
        this.accountsAPI = accountsAPI;
        this.transferMetrics = transferMetrics;
    }

    public Mono<Void> transfer(List<BalanceTransfer> transfers) {
        return accountsAPI.post().uri("/transfer")
                .bodyValue(transfers)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(any -> transferMetrics.onTransferAttempt(transfers, true))
                .doOnSuccess(any -> transferMetrics.onTransferAttempt(transfers, false))
                .then();
    }
}
