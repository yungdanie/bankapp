package ru.practicum.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.common.Event;
import ru.practicum.common.KafkaTopic;

import java.util.UUID;

@Service
@Slf4j
public class NotificationService {

    private final KafkaTemplate<UUID, Event> kafkaTemplate;

    public NotificationService(KafkaTemplate<UUID, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Event event) {
        log.info("Send notification: {}", event);
        kafkaTemplate.send(KafkaTopic.NOTIFICATIONS.name(), UUID.randomUUID(), event);
    }
}
