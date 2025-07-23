package ru.practicum.accounts;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.AccountRepository;
import ru.practicum.accounts.repository.UserRepository;
import ru.practicum.accounts.service.AccountService;

import java.time.LocalDate;

@SpringBootApplication
@AllArgsConstructor
public class AccountsApplication {

    public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}
