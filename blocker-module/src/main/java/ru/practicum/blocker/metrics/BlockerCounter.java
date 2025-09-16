package ru.practicum.blocker.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlockerCounter {

    private final MeterRegistry registry;

    public void onNotificationSendFailure() {
        Counter.builder("request_blocked_total")
                .register(registry)
                .increment();
    }
}
