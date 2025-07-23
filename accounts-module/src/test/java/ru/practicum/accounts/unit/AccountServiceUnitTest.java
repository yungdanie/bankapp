package ru.practicum.accounts.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.accounts.model.Account;
import ru.practicum.accounts.repository.AccountRepository;
import ru.practicum.accounts.service.AccountService;
import ru.practicum.accounts.service.NotificationService;
import ru.practicum.accounts.service.UserService;
import ru.practicum.common.dto.BalanceTransfer;
import ru.practicum.common.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = AccountService.class)
@ActiveProfiles("test")
class AccountServiceUnitTest {

    @Autowired
    private AccountService accountService;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserService userService;

    private static final String USERNAME_1 = "user1";

    private static final String USERNAME_2 = "user2";

    private static final String TRANSFER_CURRENCY = "USD";

    @Test
    void MINUS_100USD_TRANSFER_TEST() {
        Mockito.when(accountRepository.findByUserLoginAndCurrencyCode(USERNAME_1, "USD"))
                .thenReturn(new Account(null, null, BigDecimal.valueOf(100)));

        Assertions.assertDoesNotThrow(() -> accountService.transfer(List.of(new BalanceTransfer(USERNAME_1, TRANSFER_CURRENCY, BigDecimal.valueOf(100).negate()))));
    }

    @Test
    void PLUS_100USD_TRANSFER_TEST() {
        Mockito.when(accountRepository.findByUserLoginAndCurrencyCode(USERNAME_1, "USD"))
                .thenReturn(new Account(null, null, BigDecimal.valueOf(100)));

        Assertions.assertDoesNotThrow(() -> accountService.transfer(List.of(new BalanceTransfer(USERNAME_1, TRANSFER_CURRENCY, BigDecimal.valueOf(100)))));
    }

    @Test
    void INTER_USER_100USD_TRANSFER_TEST() {
        var account1 = new Account(null, null, BigDecimal.valueOf(100));
        var account2 = new Account(null, null, BigDecimal.valueOf(0));

        Mockito.when(accountRepository.findByUserLoginAndCurrencyCode(USERNAME_1, "USD"))
                .thenReturn(account1);

        Mockito.when(accountRepository.findByUserLoginAndCurrencyCode(USERNAME_2, "USD"))
                .thenReturn(account2);

        Assertions.assertDoesNotThrow(() -> accountService.transfer(
                List.of(
                        new BalanceTransfer(USERNAME_1, TRANSFER_CURRENCY, BigDecimal.valueOf(50).negate()),
                        new BalanceTransfer(USERNAME_2, TRANSFER_CURRENCY, BigDecimal.valueOf(50))
                )
        ));

        Assertions.assertEquals(account1.getBalance(), BigDecimal.valueOf(50));
        Assertions.assertEquals(account2.getBalance(), BigDecimal.valueOf(50));
    }

    @Test
    void NOT_ENOUGH_TRANSFER_TEST() {
        Mockito.when(accountRepository.findByUserLoginAndCurrencyCode(USERNAME_1, "USD"))
                .thenReturn(new Account(null, null, BigDecimal.valueOf(100)));

        Assertions.assertThrows(
                BadRequestException.class,
                () -> accountService.transfer(List.of(new BalanceTransfer(USERNAME_1, TRANSFER_CURRENCY, BigDecimal.valueOf(99999).negate())))
        );
    }
}
