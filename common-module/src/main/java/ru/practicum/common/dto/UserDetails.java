package ru.practicum.common.dto;

import java.util.Set;

public record UserDetails(String login, String password, Set<String> authorities) {}
