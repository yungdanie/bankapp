package ru.practicum.accounts.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.accounts.service.AccountService;
import ru.practicum.common.dto.AccountDTO;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public void updateAccounts(@RequestParam String login, @RequestBody List<AccountDTO> accounts) {
        accountService.updateAccount(login, accounts);
    }
}
