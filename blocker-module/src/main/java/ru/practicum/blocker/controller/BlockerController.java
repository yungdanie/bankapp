package ru.practicum.blocker.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.blocker.metrics.BlockerCounter;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/check")
@AllArgsConstructor
public class BlockerController {

    private final BlockerCounter blockerCounter;

    @GetMapping
    public ResponseEntity<String> check() {
        ResponseEntity<String> response = LocalTime.now().isAfter(LocalTime.of(23, 0))
                && LocalTime.now().isBefore(LocalTime.of(0, 0)) ?
                ResponseEntity.badRequest().build() : ResponseEntity.ok().build();

        if (response.getStatusCode().is4xxClientError()) {
            blockerCounter.onNotificationSendFailure();
        }

        return response;
    }
}
