package ru.practicum.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.Error;
import ru.practicum.common.dto.SignupForm;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.ui.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public Mono<String> getSignup() {
        return Mono.just("signup");
    }

    @PostMapping
    public Mono<Rendering> processSignup(ServerWebExchange exchange, @ModelAttribute SignupForm signupForm) {
        return userService.signup(signupForm, exchange)
                .thenReturn(Rendering.view("main").build())
                .onErrorResume(
                        BadRequestException.class,
                        e -> Mono.just(Rendering.view("signup")
                                .modelAttribute("errors", List.of(e.getMessage()))
                                .build()
                        )
                );
    }
}
