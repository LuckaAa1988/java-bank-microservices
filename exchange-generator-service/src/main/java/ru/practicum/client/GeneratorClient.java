package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.RateDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneratorClient {

    private final WebClient webClient;

    public void send(List<RateDto> rates) {
        webClient.post()
                .uri("/api/rates")
                .bodyValue(rates)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
