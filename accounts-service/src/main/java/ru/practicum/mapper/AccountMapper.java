package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toDto(Account account);
}
