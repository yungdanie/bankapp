package ru.practicum.notifications.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.common.Event;
import ru.practicum.notifications.metrics.NotificationsMetrics;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationService {

    private final NotificationsMetrics notificationsMetrics;

    public void processNotification(UUID uuid, Event event) {
        log.info("Processing notification for event {}", event);

        // do nothing

        // рандомно отправляем метрики, что не смогли отправить метрики
        if (ThreadLocalRandom.current().nextBoolean()) {
            notificationsMetrics.onNotificationSendFailure(event);
        }
    }
}
