package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    private String username;
    private String data;
    private String exception;
}
