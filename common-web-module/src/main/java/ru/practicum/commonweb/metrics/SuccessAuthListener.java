package ru.practicum.commonweb.metrics;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

public class SuccessAuthListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AuthMetrics authMetrics;

    public SuccessAuthListener(AuthMetrics authMetrics) {
        this.authMetrics = authMetrics;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        var auth = event.getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        authMetrics.onLoginAttempt(username, true);
    }
}
