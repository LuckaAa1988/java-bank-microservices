package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.dto.NotificationResponse;
import ru.practicum.model.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toDto(Notification notification);
}
