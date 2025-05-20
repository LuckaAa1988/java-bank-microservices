package ru.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.configuration.TestSecurityConfig;
import ru.practicum.model.dto.AccountDto;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.dto.AccountTransferRequest;
import ru.practicum.model.dto.UserDto;
import ru.practicum.service.AccountService;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@WebFluxTest(AccountController.class)
@Import({TestSecurityConfig.class})
public class AccountControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    AccountService accountService;

    @Test
    void getAll_shouldReturnAccounts() {
        String username = "test";
        AccountResponse response = AccountResponse.builder()
                .currency("USD")
                .amount(BigDecimal.valueOf(1000))
                .build();

        when(accountService.getAll(username)).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/accounts/users/{username}", username)
                .headers(header -> header.setBearerAuth("mock-token"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponse.class)
                .hasSize(1)
                .contains(response);

        verify(accountService, times(1)).getAll(any(String.class));
    }

    @Test
    void addAccount_shouldReturnAccount() {
        UserDto dto = UserDto.builder()
                .currency("USD")
                .username("test")
                .build();
        AccountResponse response = AccountResponse.builder()
                .currency("USD")
                .amount(BigDecimal.valueOf(1000))
                .build();
        when(accountService.addAccount(dto.getUsername(), dto.getCurrency()))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/accounts")
                .headers(header -> header.setBearerAuth("mock-token"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .isEqualTo(response);
    }

    @Test
    void deleteAccount_shouldReturnVoid() {
        UserDto dto = UserDto.builder()
                .currency("USD")
                .username("test")
                .build();
        when(accountService.deleteAccount(dto.getUsername(), dto.getCurrency()))
                .thenReturn(Mono.empty());

        webTestClient.method(HttpMethod.DELETE)
                .uri("/api/accounts")
                .headers(header -> header.setBearerAuth("mock-token"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deposit_shouldReturnVoid() {
        AccountDto dto = AccountDto.builder()
                .username("test")
                .currency("USD")
                .amount(BigDecimal.valueOf(1000))
                .build();
        when(accountService.deposit(dto.getUsername(), dto.getCurrency(), dto.getAmount()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/accounts/deposit")
                .headers(header -> header.setBearerAuth("mock-token"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void withdraw_shouldReturnVoid() {
        AccountDto dto = AccountDto.builder()
                .username("test")
                .currency("USD")
                .amount(BigDecimal.valueOf(1000))
                .build();
        when(accountService.withdraw(dto.getUsername(), dto.getCurrency(), dto.getAmount()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/accounts/withdraw")
                .headers(header -> header.setBearerAuth("mock-token"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void transfer_shouldReturnVoid() {
        AccountTransferRequest req = AccountTransferRequest.builder()
                .fromUser("test1")
                .toUser("test2")
                .fromAccount("USD")
                .toAccount("USD")
                .depositAmount(BigDecimal.ONE)
                .withdrawAmount(BigDecimal.ONE)
                .build();
        when(accountService.transfer(
                req.getFromUser(), req.getToUser(),
                req.getFromAccount(), req.getToAccount(),
                req.getDepositAmount(), req.getWithdrawAmount()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/accounts/transfer")
                .headers(header -> header.setBearerAuth("mock-token"))
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk();
    }

}
