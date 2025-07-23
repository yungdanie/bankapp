package ru.practicum.accounts.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.practicum.accounts.mapper.UserMapper;
import ru.practicum.accounts.model.Account;
import ru.practicum.accounts.model.User;
import ru.practicum.accounts.repository.UserRepository;
import ru.practicum.common.Currency;
import ru.practicum.common.dto.*;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.common.exception.UserValidateException;
import ru.practicum.common.exception.ValidateException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final JwtService jwtService;

    public List<UserDTO> getUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(this::getDTO)
                .toList();
    }

    public User getUser(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public UserDTO getUserDTO(String login) {
        var user = userRepository.findByLogin(login);

        if (user == null) {
            throw new BadRequestException("Пользователь не существует");
        }

        return getDTO(user);
    }

    public UserDTO getDTO(User user) {
        var accounts = accountService.findUserAccounts(user);
        List<AccountDTO> accountDTOS = accounts.stream().map(this::getDTO).toList();
        return new UserDTO(user.getLogin(), user.getName(), user.getBirthdate(), accountDTOS);
    }

    public AccountDTO getDTO(Account account) {
        var currency = Arrays.stream(Currency.values())
                .filter(curr -> curr.getCode().equals(account.getCurrencyCode()))
                .findAny().orElseThrow();

        return new AccountDTO(
                account.getBalance(),
                currency.getName(),
                currency.getCode(),
                false
        );
    }

    public TokenResponse login(LoginForm form) throws BadRequestException {
        validate(form);

        var user = userRepository.findByLogin(form.login());

        if (user == null) {
            throw new BadRequestException("Пользователь с таким именем не найден");
        }

        if (!passwordEncoder.matches(form.rawPassword(), user.getPassword())) {
            throw new BadRequestException("Пароль неверный");
        }

        return new TokenResponse(jwtService.generateTokenForUser(user.getLogin()), user.getLogin());
    }

    public void changePassword(ChangePasswordForm form) {
        validate(form);

        var user = userRepository.findByLogin(form.login());

        if (user == null) {
            throw new IllegalArgumentException("Неверный логин");
        }

        user.setPassword(passwordEncoder.encode(form.password()));
    }

    @Transactional
    public TokenResponse signup(SignupForm form) throws UserValidateException {
        validate(form);

        var user = userMapper.toUser(form);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        accountService.onNewUser(user);

        return new TokenResponse(jwtService.generateTokenForUser(user.getLogin()), user.getLogin());
    }

    private void validate(ChangePasswordForm changePasswordForm) {
        if (
                changePasswordForm.confirmPassword() == null
                || changePasswordForm.login() == null
                || changePasswordForm.password() == null
        ) {
            throw new ValidateException("Все поля формы должны быть заполнены");
        }

        if (!changePasswordForm.confirmPassword().equals(changePasswordForm.password())) {
            throw new ValidateException("Пароли не совпадают");
        }
    }

    private void validate(LoginForm form) {
        if (
                form.login() == null
                || form.rawPassword() == null || form.rawPassword().isEmpty() || form.rawPassword().isBlank()
        ) {
            throw new ValidateException("Все поля формы должны быть заполнены");
        }
    }

    private void validate(SignupForm form) throws UserValidateException {
        if (
                form.login() == null
                || form.birthdate() == null
                || form.password() == null
                || form.name() == null
                || form.confirmPassword() == null) {
            throw new UserValidateException("Все поля формы обязательны для заполнения");
        }

        if (!form.password().equals(form.confirmPassword())) {
            throw new UserValidateException("Пароли не совпадают");
        }

        if (userRepository.findByLogin(form.login()) != null) {
            throw new UserValidateException("Такой логин уже существует в системе");
        }

        if (form.birthdate().plusYears(18).isAfter(LocalDate.now())) {
            throw new UserValidateException("Пользователю должно быть больше 18 лет");
        }
    }
}
