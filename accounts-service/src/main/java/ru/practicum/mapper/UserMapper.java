package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.dto.UserResponse;
import ru.practicum.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User fromDto(UserRegistrationDto userRegistrationDto);
    UserResponse toDto(User user);
}
