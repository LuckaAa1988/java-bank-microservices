package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountTransferRequest;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;

@Component
@RequiredArgsConstructor
public class AccountsClient {

    private final WebClient.Builder webclient;

    public Mono<Void> transfer(AccountTransferRequest accountTransferRequest) {
        return webclient.baseUrl("http://api-gateway:8080/api/accounts/transfer").build()
                .post()
                .bodyValue(accountTransferRequest)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
