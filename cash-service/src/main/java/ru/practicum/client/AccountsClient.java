package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountDto;


@Component
@RequiredArgsConstructor
public class AccountsClient {

    private final WebClient.Builder webclient;
    public Mono<Void> deposit(AccountDto accountDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/accounts").build()
                .post()
                .uri("/deposit")
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> withdraw(AccountDto accountDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/accounts").build()
                .post()
                .uri("/withdraw")
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
