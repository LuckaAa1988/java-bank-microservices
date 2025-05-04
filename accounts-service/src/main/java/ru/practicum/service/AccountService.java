package ru.practicum.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountResponse;

import java.math.BigDecimal;

public interface AccountService {
    Flux<AccountResponse> getAll(String username);

    Mono<AccountResponse> addAccount(String name, String currency);

    Mono<Void> deleteAccount(String username, String currency);

    Mono<Void> deposit(String username, String currency, BigDecimal amount);

    Mono<Void> withdraw(String username, String currency, BigDecimal amount);
}
