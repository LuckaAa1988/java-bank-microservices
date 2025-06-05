package ru.practicum.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.model.dto.NotificationResponse;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    void addNotification(NotificationDto notificationDto);

    Flux<NotificationResponse> getNotifications(String username);
}
