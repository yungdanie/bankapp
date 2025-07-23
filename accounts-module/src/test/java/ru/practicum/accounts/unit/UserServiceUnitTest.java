package ru.practicum.accounts.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.accounts.mapper.UserMapper;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.UserRepository;
import ru.practicum.accounts.service.AccountService;
import ru.practicum.accounts.service.JwtService;
import ru.practicum.accounts.service.UserService;
import ru.practicum.common.dto.LoginForm;
import ru.practicum.common.exception.BadRequestException;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(classes = UserService.class)
@ActiveProfiles("test")
class UserServiceUnitTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void badPasswordLoginTest() {
        Mockito.when(jwtService.generateTokenForUser(any(String.class))).thenReturn("token");
        Mockito.when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(a -> a.getArgument(0, String.class).equals(a.getArgument(1, String.class)));
        var user = new User("user", "password", "user", LocalDate.now());
        var loginForm = new LoginForm("user", "badPassword");
        Mockito.when(userRepository.findByLogin("user")).thenReturn(user);
        Assertions.assertThrows(BadRequestException.class, () -> userService.login(loginForm));
    }

    @Test
    void loginTest() {
        Mockito.when(jwtService.generateTokenForUser(any(String.class))).thenReturn("token");
        Mockito.when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(a -> a.getArgument(0, String.class).equals(a.getArgument(1, String.class)));
        var user = new User("user", "password", "user", LocalDate.now());
        var loginForm = new LoginForm("user", "password");
        Mockito.when(userRepository.findByLogin("user")).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> userService.login(loginForm));
    }
}
