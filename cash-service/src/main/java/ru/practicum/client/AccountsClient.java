package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountDto;


@Component
@RequiredArgsConstructor
public class AccountsClient {

    private final WebClient webclient;
    public Mono<Void> deposit(AccountDto accountDto) {
        return webclient.post()
                .uri("/api/accounts/deposit")
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> withdraw(AccountDto accountDto) {
        return webclient.post()
                .uri("/api/accounts/withdraw")
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
