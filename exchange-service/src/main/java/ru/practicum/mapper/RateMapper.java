package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.dto.RateDto;
import ru.practicum.model.entity.Rate;

@Mapper(componentModel = "spring")
public interface RateMapper {
    RateDto toDto(Rate rate);
    Rate fromDto(RateDto rateDto);
}
