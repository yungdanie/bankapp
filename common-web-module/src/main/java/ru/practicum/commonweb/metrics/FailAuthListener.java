package ru.practicum.commonweb.metrics;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

public class FailAuthListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final AuthMetrics authMetrics;

    public FailAuthListener(AuthMetrics authMetrics) {
        this.authMetrics = authMetrics;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        var auth = event.getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        authMetrics.onLoginAttempt(username, false);
    }
}
