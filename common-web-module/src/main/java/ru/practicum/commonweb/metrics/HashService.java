package ru.practicum.commonweb.metrics;

import java.util.Objects;

/**
 * Представим что сервис очень по-умному хеширует данные
 */
public class HashService {

    protected HashService() {}

    public static String getUsernameHash(final String username) {
        return String.valueOf(Objects.requireNonNull(username).hashCode());
    }
}
