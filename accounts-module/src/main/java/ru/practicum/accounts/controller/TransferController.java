package ru.practicum.accounts.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.accounts.service.AccountService;
import ru.practicum.common.dto.BalanceTransfer;

import java.util.List;

@RestController
@AllArgsConstructor
public class TransferController {

    private final AccountService accountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/api/transfer")
    public void transfer(@RequestBody List<BalanceTransfer> balanceTransfers) {
        accountService.transfer(balanceTransfers);
    }
}
