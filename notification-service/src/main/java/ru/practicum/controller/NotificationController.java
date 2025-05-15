package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.model.dto.NotificationResponse;
import ru.practicum.service.NotificationService;

import java.security.Principal;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Flux<NotificationResponse> streamNotifications(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username");
        return notificationService.getNotifications(username);

    }

    @PostMapping
    public Mono<ResponseEntity<Mono<Void>>> addNotification(@RequestBody NotificationDto notificationDto) {
        return Mono.just(ResponseEntity.ok(notificationService.addNotification(notificationDto)));
    }
}
