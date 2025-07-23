package ru.practicum.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.accounts.controller.SignupController;
import ru.practicum.accounts.controller.TransferController;
import ru.practicum.accounts.service.AccountService;
import ru.practicum.accounts.service.UserService;

@WebMvcTest(controllers = {TransferController.class, SignupController.class})
public abstract class ContractVerifierBase {

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        // Настраиваем RestAssuredMockMvc с нужным контекстом
        io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context).build()
        );
    }

}
