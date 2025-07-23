package ru.practicum.ui.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.AccountDTO;
import ru.practicum.common.dto.ChangePasswordForm;
import ru.practicum.common.dto.Transfer;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.ui.dto.CashActionDTO;
import ru.practicum.ui.service.AccountService;
import ru.practicum.ui.service.PageService;
import ru.practicum.ui.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserActionController {

    private final UserService userService;

    private final AccountService accountService;

    private final PageService pageService;

    @PostMapping("/editPassword")
    public Mono<Rendering> editPassword(@ModelAttribute ChangePasswordForm changePasswordForm) {
        return userService.editPassword(changePasswordForm)
                .then(pageService.getMainPageBuilder().map(Rendering.Builder::build))
                .onErrorResume(
                        BadRequestException.class,
                        e -> pageService.getMainPageBuilder()
                                .map(builder ->
                                        builder.modelAttribute(
                                                "passwordErrors",
                                                List.of(e.getMessage())
                                        ).build()
                                )
                );
    }

    @PostMapping(path = "/editAccounts")
    public Mono<Rendering> editAccounts(List<AccountDTO> accounts) {
        return accountService.editAccounts(accounts)
                .then(pageService.getMainPageBuilder().map(Rendering.Builder::build))
                .onErrorResume(
                        BadRequestException.class,
                        e -> pageService.getMainPageBuilder()
                                .map(builder ->
                                        builder.modelAttribute(
                                                "userAccountsError",
                                                List.of(e.getMessage())
                                        ).build()
                                )
                );
    }

    @PostMapping("/cash")
    public Mono<Rendering> cash(@ModelAttribute CashActionDTO cashActionDTO) {
        return (
                cashActionDTO.action().equals("PUT") ?
                        accountService.deposit(cashActionDTO) :
                        accountService.withdraw(cashActionDTO)
        )
                .then(pageService.getMainPageBuilder().map(Rendering.Builder::build))
                .onErrorResume(
                        BadRequestException.class,
                        e -> pageService.getMainPageBuilder()
                                .map(builder ->
                                        builder.modelAttribute(
                                                "cashErrors",
                                                List.of(e.getMessage())
                                        ).build()
                                )
                );
    }

    @PostMapping("/innerTransfer")
    public Mono<Rendering> innerTransfer(@ModelAttribute Transfer transfer) {
        return accountService.innerTransfer(
                        transfer.toCurrencyCode(),
                        transfer.fromCurrencyCode(),
                        transfer.amount()
                )
                .then(pageService.getMainPageBuilder().map(Rendering.Builder::build))
                .onErrorResume(
                        BadRequestException.class,
                        e -> pageService.getMainPageBuilder()
                                .map(builder ->
                                        builder.modelAttribute(
                                                "transferErrors",
                                                List.of(e.getMessage())
                                        ).build()
                                )
                );
    }

    @PostMapping("/transfer")
    public Mono<Rendering> transfer(@ModelAttribute Transfer transfer) {
        return accountService.transfer(
                        transfer.toLogin(),
                        transfer.toCurrencyCode(),
                        transfer.fromCurrencyCode(),
                        transfer.amount()
                )
                .then(pageService.getMainPageBuilder().map(Rendering.Builder::build))
                .onErrorResume(
                        BadRequestException.class,
                        e -> pageService.getMainPageBuilder()
                                .map(builder ->
                                        builder.modelAttribute(
                                                "transferOtherErrors",
                                                List.of(e.getMessage())
                                        ).build()
                                )
                );
    }

}
