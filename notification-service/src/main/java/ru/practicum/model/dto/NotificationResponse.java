package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    String message;
    String type;
}
