package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;

@Component
@RequiredArgsConstructor
public class ExchangeClient {

    private final WebClient.Builder webclient;

    public Mono<RateDto> getRate(String currency) {
        return webclient.baseUrl("http://api-gateway:8080/api/rates").build()
                .get()
                .uri("/{currency}", currency)
                .retrieve()
                .bodyToMono(RateDto.class);
    }
}
