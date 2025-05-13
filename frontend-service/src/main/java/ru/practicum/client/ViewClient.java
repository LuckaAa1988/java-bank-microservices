package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
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
                .uri("/rates")
                .retrieve()
                .bodyToFlux(RateDto.class);
    }

    public Mono<UserResponse> getUser(String username) {
        return webclient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }

    public Flux<AccountResponse> getAccounts(String username) {
        return webclient.get()
                .uri("/accounts/users/{username}", username)
                .retrieve()
                .bodyToFlux(AccountResponse.class);
    }
}
