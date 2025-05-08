package ru.practicum.service;

import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountDto;

public interface CashService {
    Mono<Void> deposit(AccountDto accountDto);

    Mono<Void> withdraw(AccountDto accountDto);
}
