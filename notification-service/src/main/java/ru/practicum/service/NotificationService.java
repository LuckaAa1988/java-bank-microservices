package ru.practicum.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.model.dto.NotificationResponse;

public interface NotificationService {
    Mono<Void> addNotification(NotificationDto notificationDto);

    Flux<NotificationResponse> getNotifications(String username);
}
