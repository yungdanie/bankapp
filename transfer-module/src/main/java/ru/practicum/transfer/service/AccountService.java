package ru.practicum.transfer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.BalanceTransfer;

import java.util.List;

@Service
public class AccountService {

    private final WebClient accountsAPI;

    public AccountService(@Qualifier("accountsAPI") WebClient accountsAPI) {
        this.accountsAPI = accountsAPI;
    }

    public Mono<Void> transfer(List<BalanceTransfer> transfers) {
        return accountsAPI.post().uri("/transfer")
                .bodyValue(transfers)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
