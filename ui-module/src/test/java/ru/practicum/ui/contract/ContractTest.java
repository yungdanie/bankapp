package ru.practicum.ui.contract;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.StubRunner;
import org.springframework.cloud.contract.stubrunner.StubRunnerOptions;
import org.springframework.cloud.contract.stubrunner.StubRunnerOptionsBuilder;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ContractTest.TestConfig.class)
@AutoConfigureStubRunner(
        ids = "ru.practicum:accounts-module:+:stubs:9999",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@ActiveProfiles("test")
class ContractTest {

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:9999").build();

    @Configuration
    static class TestConfig {}

    @Test
    void signupTest() {
        var requestBody =   Map.of(
                "login", "admin",
                "confirmPassword", "admin",
                "password", "admin",
                "name", "name",
                "birthdate", "2000-10-01"
        );

        var response = webClient.post().uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();

        Assertions.assertNotNull(response);
    }

    @Test
    void postTransferTest() {
        var requestBody = List.of(
                Map.of(
                        "login", "admin",
                        "currencyCode", "643",
                        "amount", 100
                )
        );

        var response = webClient.post().uri("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();

        Assertions.assertNotNull(response);
    }

}
