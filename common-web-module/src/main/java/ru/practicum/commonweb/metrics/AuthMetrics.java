package ru.practicum.commonweb.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class AuthMetrics {

    private final MeterRegistry registry;

    public AuthMetrics(MeterRegistry registry) { this.registry = registry; }

    public void onLoginAttempt(String username, boolean success) {
        Counter.builder("auth_login_attempts_total")
                .tag("result", success ? "success" : "fail")
                .tag("user_cohort", HashService.getUsernameHash(username))
                .register(registry)
                .increment();
    }
}
