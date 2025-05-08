package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.NotificationDto;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final WebClient.Builder webclient;

    public Mono<Void> sendFailureNotification(NotificationDto notificationDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/notifications").build()
                .post()
                .bodyValue(notificationDto)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> sendSuccessNotification(NotificationDto notificationDto) {
        return webclient.baseUrl("http://api-gateway:8080/api/notifications").build()
                .post()
                .bodyValue(notificationDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
