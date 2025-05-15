package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mapper.NotificationMapper;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.model.dto.NotificationResponse;
import ru.practicum.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Void> addNotification(NotificationDto notificationDto) {
        var username = notificationDto.getUsername();
        var type = notificationDto.getException() == null ? "SUCCESS" : "FAIL";
        return databaseClient.sql("INSERT INTO notifications (username, message, type, showed) " +
                        "VALUES (:username, :message, :type, :showed)")
                .bind("username", username)
                .bind("message", notificationDto.getData())
                .bind("type", type)
                .bind("showed", false)
                .fetch()
                .one().then();
    }

    @Override
    public Flux<NotificationResponse> getNotifications(String username) {
        return databaseClient.sql(
                        "SELECT message, type FROM notifications WHERE username = :username AND showed = 'FALSE'")
                .bind("username", username)
                .map((row, metadata) -> NotificationResponse.builder()
                        .message(row.get("message", String.class))
                        .type(row.get("type", String.class))
                        .build())
                .all()
                .collectList()
                .flatMapMany(list -> databaseClient.sql(
                                "UPDATE notifications SET showed = 'TRUE' WHERE username = :username")
                        .bind("username", username)
                        .fetch()
                        .rowsUpdated()
                        .thenMany(Flux.fromIterable(list)));
    }
}
