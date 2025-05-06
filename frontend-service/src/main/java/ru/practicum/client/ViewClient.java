package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.dto.RateDto;
import ru.practicum.model.dto.UserResponse;

@Component
@RequiredArgsConstructor
public class ViewClient {

    private final WebClient.Builder webclient;

    public Flux<RateDto> getRates() {
        return webclient.baseUrl("http://api-gateway:8080/api/rates").build()
                .get()
                .retrieve()
                .bodyToFlux(RateDto.class);
    }

    public Mono<UserResponse> getUser(String username) {
        return webclient.baseUrl("http://api-gateway:8080/api/users").build()
                .get()
                .uri("/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Flux<AccountResponse> getAccounts(String username) {
        return webclient.baseUrl("http://api-gateway:8080/api/accounts/users").build()
                .get()
                .uri("/{username}", username)
                .retrieve()
                .bodyToFlux(AccountResponse.class);
    }
}
