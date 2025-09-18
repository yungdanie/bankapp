package ru.practicum.notifications.service;

import org.springframework.stereotype.Service;
import ru.practicum.common.Event;

import java.util.UUID;

@Service
public class NotificationService {

    public void processNotification(UUID uuid, Event event) {
        // do nothing
    }
}
