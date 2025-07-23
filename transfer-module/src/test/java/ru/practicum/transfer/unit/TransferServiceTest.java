package ru.practicum.transfer.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.Transfer;
import ru.practicum.common.exception.ValidateException;
import ru.practicum.transfer.service.AccountService;
import ru.practicum.transfer.service.ExchangeService;
import ru.practicum.transfer.service.TransferService;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = TransferService.class)
class TransferServiceTest {

    @MockitoBean("blockerAPI")
    WebClient webClient;

    @MockitoBean
    ExchangeService exchangeService;

    @MockitoBean
    AccountService accountService;

    @Autowired
    TransferService transferService;

    @BeforeEach
    public void beforeEach() {
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        ResponseEntity<Void> responseEntity = mock(ResponseEntity.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/check")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.just(responseEntity));
    }

    @Test
    void notValidCurrencyCodeTest() {
        Transfer transfer = new Transfer("admin", "admin", null, null, BigDecimal.ONE);
        Assertions.assertThrows(ValidateException.class, () -> transferService.transfer(transfer).block());
    }

    @Test
    void notValidLoginCodeTest() {
        Transfer transfer = new Transfer(null, "admin", "643", "643", BigDecimal.ONE);
        Assertions.assertThrows(ValidateException.class, () -> transferService.transfer(transfer).block());
    }
}
