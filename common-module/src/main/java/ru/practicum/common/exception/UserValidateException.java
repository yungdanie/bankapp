package ru.practicum.common.exception;

public class UserValidateException extends BadRequestException {

    public UserValidateException(String message) {
        super(message);
    }
}
