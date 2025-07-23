package ru.practicum.accounts.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.accounts.service.UserService;
import ru.practicum.common.dto.LoginForm;
import ru.practicum.common.dto.TokenResponse;

@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping
    public TokenResponse login(@RequestBody LoginForm loginForm) throws BadRequestException {
        return userService.login(loginForm);
    }
}
