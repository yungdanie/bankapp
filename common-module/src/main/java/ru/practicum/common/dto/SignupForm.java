package ru.practicum.common.dto;

import java.time.LocalDate;

public record SignupForm(String password, String confirmPassword, String name, String login, LocalDate birthdate) {}
