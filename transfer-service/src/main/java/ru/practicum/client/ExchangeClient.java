package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;

@Component
@RequiredArgsConstructor
public class ExchangeClient {

    private final WebClient webclient;

    public Mono<RateDto> getRate(String currency) {
        return webclient.get()
                .uri("/api/rates/{currency}", currency)
                .retrieve()
                .bodyToMono(RateDto.class);
    }
}
