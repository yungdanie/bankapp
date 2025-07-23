package ru.practicum.common.dto;

public record ChangePasswordForm(String login, String password, String confirmPassword) { }
