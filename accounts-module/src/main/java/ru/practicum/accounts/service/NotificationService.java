package ru.practicum.accounts.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.common.Event;

@Service
public class NotificationService {

    private final RestClient notificationAPI;

    public NotificationService(@Qualifier("notificationAPI") RestClient notificationAPI) {
        this.notificationAPI = notificationAPI;
    }

    public void sendNotification(Event event) {
        notificationAPI.post().body(event).retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {})
                .toBodilessEntity();
    }
}
