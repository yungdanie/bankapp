package ru.practicum.transfer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.commonweb.metrics.HashService;

import java.util.List;
import java.util.Objects;

@Service
public class TransferMetrics {

    private final MeterRegistry registry;

    public TransferMetrics(MeterRegistry registry) { this.registry = registry; }

    public void onTransferAttempt(List<BalanceTransfer> transfers, boolean success) {
        var firstTransfer = transfers.getFirst();
        var lastTransfer = transfers.getLast();

        if (firstTransfer != null && lastTransfer != null) {
            onTransferAttempt(firstTransfer.login(), lastTransfer.login(), success, Objects.equals(firstTransfer.login(), lastTransfer.login()));
        }
    }

    private void onTransferAttempt(String senderLogin, String receiverLogin, boolean success, boolean internal) {
        Counter.builder("transfer_attempts_total")
                .tag("result", success ? "success" : "fail")
                .tag("kind", internal ? "internal" : "external")
                .tag("sender_cohort", HashService.getUsernameHash(senderLogin))
                .tag("receiver_cohort", HashService.getUsernameHash(receiverLogin))
                .register(registry)
                .increment();
    }
}
