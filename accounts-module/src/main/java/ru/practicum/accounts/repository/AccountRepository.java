package ru.practicum.accounts.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.accounts.model.Account;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("select a from Account a where a.currencyCode = :currencyCode and a.user.login = :login")
    Account findByUserLoginAndCurrencyCode(@Value("login") String login, @Value("currencyCode") String currencyCode);

    @Query("SELECT a from Account  a where a.user.id = :userId and a.deleted is false")
    List<Account> findByUserId(@Value("userId") Long userId);
}
