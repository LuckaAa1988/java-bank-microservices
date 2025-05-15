package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.dto.RateDto;
import ru.practicum.model.dto.UserResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewClient {

    private final WebClient webclient;

    public Flux<RateDto> getRates() {
        return webclient.get()
                .uri("/api/rates")
                .retrieve()
                .bodyToFlux(RateDto.class);
    }

    public Mono<UserResponse> getUser(String username) {
        return webclient.get()
                .uri("/api/users/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Flux<AccountResponse> getAccounts(String username) {
        return webclient.get()
                .uri("/api/accounts/users/{username}", username)
                .retrieve()
                .bodyToFlux(AccountResponse.class);
    }
}
