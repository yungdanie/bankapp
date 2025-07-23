package ru.practicum.accounts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.common.exception.BadRequestException;
import ru.practicum.common.dto.Error;
import ru.practicum.common.exception.ValidateException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Error> handleValidationException(BadRequestException e) {
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<Error> handleValidationException(ValidateException e) {
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }
}
