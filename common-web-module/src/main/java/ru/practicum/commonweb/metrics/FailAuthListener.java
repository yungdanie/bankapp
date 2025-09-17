package ru.practicum.commonweb.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

@Slf4j
public class FailAuthListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final AuthMetrics authMetrics;

    public FailAuthListener(AuthMetrics authMetrics) {
        this.authMetrics = authMetrics;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        var auth = event.getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        log.info("Failed to authenticate user: {}", username);
        authMetrics.onLoginAttempt(username, false);
    }
}
