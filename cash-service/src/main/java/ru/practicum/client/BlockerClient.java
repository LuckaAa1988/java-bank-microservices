package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.excpetion.BlockerException;
import ru.practicum.model.dto.AccountDto;

@Component
@RequiredArgsConstructor
public class BlockerClient {

    private final WebClient.Builder webclient;

    public Mono<Void> checkDeposit(AccountDto accountDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/blocker").build()
                .post()
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> checkWithdraw(AccountDto accountDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/blocker").build()
                .post()
                .bodyValue(accountDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
