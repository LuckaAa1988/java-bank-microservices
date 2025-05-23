package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountTransferRequest;

@Component
@RequiredArgsConstructor
public class AccountsClient {

    private final WebClient webclient;

    public Mono<Void> transfer(AccountTransferRequest accountTransferRequest) {
        return webclient.post()
                .uri("/api/accounts/transfer")
                .bodyValue(accountTransferRequest)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
