package ru.practicum.ui.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.SignupForm;
import ru.practicum.common.dto.TokenResponse;
import ru.practicum.ui.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserService.class)
class UserServiceTest {

    @MockitoBean("accountsAPI")
    WebClient accountsAPI;

    @Autowired
    UserService userService;

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    void signupTest() {
        var signupForm = new SignupForm("", "", "", "", LocalDate.now());
        var tokenResponse = new TokenResponse("token", "login");

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec  responseSpec = mock(WebClient.ResponseSpec.class);

        when(accountsAPI.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/signup")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(signupForm)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TokenResponse.class))
                .thenReturn(Mono.just(tokenResponse));

        var exchange = Mockito.mock(ServerWebExchange.class);
        var response = Mockito.mock(ServerHttpResponse.class);
        var cookieMap = new HashMap<String, List<ResponseCookie>>();
        var cookie = new MultiValueMapAdapter<>(cookieMap);

        Mockito.when(exchange.getResponse()).thenReturn(response);
        Mockito.when(response.getCookies()).thenReturn(cookie);

        userService.signup(signupForm, exchange).block();

        Mockito.verify(accountsAPI, Mockito.times(1))
                .post();

        Mockito.verify(response, Mockito.times(1)).addCookie(any());
    }
}
