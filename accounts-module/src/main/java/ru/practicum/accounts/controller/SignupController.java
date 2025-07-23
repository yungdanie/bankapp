package ru.practicum.accounts.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.exception.UserValidateException;
import ru.practicum.accounts.service.UserService;
import ru.practicum.common.dto.SignupForm;
import ru.practicum.common.dto.TokenResponse;

@RestController
@RequestMapping("/api/signup")
@AllArgsConstructor
public class SignupController {

    private final UserService userService;

    @PostMapping
    public TokenResponse signup(@RequestBody final SignupForm signupForm) throws UserValidateException {
        return userService.signup(signupForm);
    }
}
