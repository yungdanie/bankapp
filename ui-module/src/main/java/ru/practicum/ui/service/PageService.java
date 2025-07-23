package ru.practicum.ui.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.practicum.common.Currency;

@Service
@AllArgsConstructor
public class PageService {

    private final UserService userService;

    public Mono<Rendering.Builder<?>> getMainPageBuilder() {
        return Mono.zip(userService.getCurrentUser(), userService.getAllUsers())
                .map(
                        tuple2 -> Rendering.view("main")
                                .modelAttribute("currencies", Currency.values())
                                .modelAttribute("user", tuple2.getT1())
                                .modelAttribute("users", tuple2.getT2())
                );
    }

}
