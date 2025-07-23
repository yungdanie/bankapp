package ru.practicum.common.dto;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(String login, String name, LocalDate birthdate, List<AccountDTO> accounts) {}
