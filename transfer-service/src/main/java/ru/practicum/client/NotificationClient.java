package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.dto.NotificationDto;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    public void sendFailureNotification(NotificationDto<?> notificationDto) {

    }

    public void sendSuccessNotification(NotificationDto<?> notificationDto) {
    }
}
