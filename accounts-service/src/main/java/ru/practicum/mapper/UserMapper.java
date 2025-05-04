package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.dto.UserResponse;
import ru.practicum.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromDto(UserRegistrationDto userRegistrationDto);
    UserResponse toDto(User user);
}
