package ru.practicum.accounts.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.accounts.model.Account;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.AccountRepository;
import ru.practicum.accounts.repository.UserRepository;
import ru.practicum.common.Event;
import ru.practicum.common.dto.AccountDTO;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.common.Currency;
import ru.practicum.common.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
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
        var user = userService.getUser(login);

        if (user == null) {
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
    }

    public void onNewUser(final User user) {
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
        for (BalanceTransfer transfer : balanceTransfer) {
            var account = accountRepository.findByUserLoginAndCurrencyCode(
                    transfer.login(),
                    transfer.currencyCode()
            );

            if (account == null) {
                throw new BadRequestException("Аккаунт не найден");
            }

            if (account.getBalance().add(transfer.amount()).compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Недостаточно средств");
            }

            account.setBalance(account.getBalance().add(transfer.amount()));
        }
    }
}
