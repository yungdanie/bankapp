package ru.practicum.exchange.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class CurrencyMetrics {

    private final AtomicLong lastUpdate = new AtomicLong(0);

    private final MeterRegistry registry;

    public void onCurrencyReceived() {
        registry.gauge(
                "fx_rates_last_update_seconds",
                Tags.of("provider","ecb"),
                lastUpdate
        );
    }
}
