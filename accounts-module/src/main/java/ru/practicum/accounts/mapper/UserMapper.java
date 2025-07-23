package ru.practicum.accounts.mapper;

import org.mapstruct.Mapper;
import ru.practicum.accounts.model.User;
import ru.practicum.common.dto.SignupForm;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(SignupForm signupForm);
}
