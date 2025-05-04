package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CashClient {

    private final WebClient.Builder webclient;
    public Mono<Void> deposit(String username, String currency, BigDecimal amount) {
        return webclient.baseUrl("http://api-gateway:8080/api/users").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{username}/deposit/{currency}")
                        .queryParam("amount", amount)
                        .build(username, currency))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> withdraw(String username, String currency, BigDecimal amount) {
        return webclient.baseUrl("http://api-gateway:8080/api/users").build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{username}/withdraw/{currency}")
                        .queryParam("amount", amount)
                        .build(username, currency))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
