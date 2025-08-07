package ru.practicum.accounts.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.UserRepository;
import ru.practicum.accounts.service.AccountService;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    private final AccountService accountService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (userRepository.findByLogin("admin") == null) {
            var user = userRepository.save(new User("admin", "admin", "admin", LocalDate.now()));
            accountService.onNewUser(user);
        }
    }
}
