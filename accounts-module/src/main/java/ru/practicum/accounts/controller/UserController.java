package ru.practicum.accounts.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.accounts.service.UserService;
import ru.practicum.common.dto.ChangePasswordForm;
import ru.practicum.common.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @RequestMapping("/user/editPassword")
    public void changePassword(@RequestBody ChangePasswordForm changePasswordForm) {
        userService.changePassword(changePasswordForm);
    }

    @GetMapping("/user")
    public UserDTO getUser(@RequestParam String login) {
        return userService.getUserDTO(login);
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }
}
