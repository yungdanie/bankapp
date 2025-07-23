package ru.practicum.notifications.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.Event;

@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping
    public void onEvent(@RequestBody Event event) {
        // do nothing
    }
}
