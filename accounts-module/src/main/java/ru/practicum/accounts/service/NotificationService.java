package ru.practicum.accounts.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.common.Event;
import ru.practicum.common.KafkaTopic;

import java.util.UUID;

@Service
public class NotificationService {

    private final KafkaTemplate<UUID, Event> kafkaTemplate;

    public NotificationService(KafkaTemplate<UUID, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Event event) {
        kafkaTemplate.send(KafkaTopic.NOTIFICATIONS.name(), UUID.randomUUID(), event);
    }
}
