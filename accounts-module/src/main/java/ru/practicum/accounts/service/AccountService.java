package ru.practicum.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.accounts.model.Account;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.AccountRepository;
import ru.practicum.common.Currency;
import ru.practicum.common.Event;
import ru.practicum.common.dto.AccountDTO;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.common.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final NotificationService notificationService;

    private final UserService userService;

    public AccountService(
            AccountRepository accountRepository,
            NotificationService notificationService,
            @Lazy UserService userService) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Transactional
    public void updateAccount(String login, List<AccountDTO> accounts) {
        log.debug("Updating account login: {}, accounts: {}", login, accounts);

        var user = userService.getUser(login);

        if (user == null) {
            log.debug("Update accounts failed, user with login: {} not found", login);
            throw new BadRequestException("User not found");
        }

        var userAccounts = findUserAccounts(user);

        accounts.forEach(accountDTO -> {
            var userAccount = userAccounts.stream()
                    .filter(acc -> acc.getCurrencyCode().equals(accountDTO.currencyCode()))
                    .findFirst()
                    .orElseThrow();
            userAccount.setDeleted(accountDTO.deleted());
        });

        log.info("Updating account success login: {}, accounts: {}", login, userAccounts);
    }

    public void onNewUser(final User user) {
        log.debug("Handle new user event login: {}", user.getLogin());

        var accounts = Arrays.stream(Currency.values())
                .map(currency -> new Account(user, currency.getCode(), BigDecimal.ZERO))
                .toList();

        accountRepository.saveAll(accounts);
        notificationService.sendNotification(Event.NEW_USER);
    }

    public List<Account> findUserAccounts(final User user) {
        return accountRepository.findByUserId(user.getId());
    }

    public void transfer(List<BalanceTransfer> balanceTransfer) {
        log.debug("Start transfer: {}", balanceTransfer);

        for (BalanceTransfer transfer : balanceTransfer) {
            var account = accountRepository.findByUserLoginAndCurrencyCode(
                    transfer.login(),
                    transfer.currencyCode()
            );

            if (account == null) {
                log.error("Account with login: {} not found", transfer.login());
                throw new BadRequestException("Аккаунт не найден");
            }

            if (account.getBalance().add(transfer.amount()).compareTo(BigDecimal.ZERO) < 0) {
                log.error("Transfer account balance overflow: {}, accountID: {}", transfer.amount(), account.getId());
                throw new BadRequestException("Недостаточно средств");
            }

            account.setBalance(account.getBalance().add(transfer.amount()));
        }

        log.info("Finish transfer: {}", balanceTransfer);
    }
}
