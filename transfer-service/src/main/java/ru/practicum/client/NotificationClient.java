package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.NotificationDto;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final WebClient webclient;

    public Mono<Void> sendFailureNotification(NotificationDto notificationDto) {
        return webclient.post()
                .uri("/api/notifications")
                .bodyValue(notificationDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> sendSuccessNotification(NotificationDto notificationDto) {
        return webclient.post()
                .uri("/api/notifications")
                .bodyValue(notificationDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
