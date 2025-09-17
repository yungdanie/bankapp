package ru.practicum.commonweb.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Slf4j
public class SuccessAuthListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AuthMetrics authMetrics;

    public SuccessAuthListener(AuthMetrics authMetrics) {
        this.authMetrics = authMetrics;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        var auth = event.getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        log.warn("Success authenticate user: {}", username);
        authMetrics.onLoginAttempt(username, true);
    }
}
