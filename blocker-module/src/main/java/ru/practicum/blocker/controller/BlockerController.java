package ru.practicum.blocker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/check")
public class BlockerController {

    @GetMapping
    public ResponseEntity<String> check() {
        return LocalTime.now().isAfter(LocalTime.of(23, 0))
                && LocalTime.now().isBefore(LocalTime.of(0, 0)) ?
                ResponseEntity.badRequest().build() : ResponseEntity.ok().build();
    }
}
