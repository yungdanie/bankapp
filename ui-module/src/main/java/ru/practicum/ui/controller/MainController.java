package ru.practicum.ui.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.practicum.ui.service.PageService;

@Controller
@AllArgsConstructor
public class MainController {

    private final PageService pageService;

    @GetMapping("/login")
    public Mono<String> login() {
        return Mono.just("signup");
    }

    @GetMapping("/")
    public Mono<String> index() {
        return Mono.just("redirect:/main");
    }

    @GetMapping("/main")
    public Mono<Rendering> mainPage() {
        return pageService.getMainPageBuilder().map(Rendering.Builder::build);
    }
}
