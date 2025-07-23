package ru.practicum.common;

import lombok.Getter;

@Getter
public enum Currency {

    RUB("643"),
    USD("840"),
    CNY("156");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getName() {
        return name();
    }
}

