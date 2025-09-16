package ru.practicum.notifications.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.Event;
import ru.practicum.notifications.metrics.NotificationsMetrics;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationsMetrics notificationsMetrics;

    public void processNotification(UUID uuid, Event event) {
        // do nothing

        // рандомно отправляем метрики, что не смогли отправить метрики
        if (ThreadLocalRandom.current().nextBoolean()) {
            notificationsMetrics.onNotificationSendFailure(event);
        }
    }
}
