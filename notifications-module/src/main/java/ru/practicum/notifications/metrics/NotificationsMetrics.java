package ru.practicum.notifications.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import ru.practicum.common.Event;

@Service
public class NotificationsMetrics {

    private final MeterRegistry registry;

    public NotificationsMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void onNotificationSendFailure(Event event) {
        Counter.builder("notifications_send_failure_total")
                .tag("event", event.name())
                .register(registry)
                .increment();
    }
}
