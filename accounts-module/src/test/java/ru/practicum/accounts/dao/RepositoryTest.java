package ru.practicum.accounts.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.accounts.model.Account;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.AccountRepository;
import ru.practicum.accounts.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
class RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void userSaveTest() {
        var user = userRepository.save(new User("user", "user", "password", LocalDate.now()));
        var dbUser = userRepository.findById(user.getId());
        Assertions.assertNotNull(dbUser);
    }

    @Test
    void accountSaveTest() {
        var user = userRepository.save(new User("user", "user", "password", LocalDate.now()));
        var account = accountRepository.save(new Account(user, "", BigDecimal.ZERO));
        Assertions.assertNotNull(accountRepository.findById(account.getId()));
    }

    @Test
    void getAccountsTest() {
        var user = userRepository.save(new User("user", "user", "password", LocalDate.now()));
        var account = accountRepository.save(new Account(user, "", BigDecimal.ZERO));

        var dbAccounts = accountRepository.findByUserId(user.getId());

        Assertions.assertNotNull(dbAccounts);
        Assertions.assertEquals(1, dbAccounts.size());
        Assertions.assertEquals(dbAccounts.getFirst(), account);
    }
}
