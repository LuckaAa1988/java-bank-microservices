package ru.practicum.service;

import reactor.core.publisher.Flux;
import ru.practicum.model.dto.RateDto;

import java.util.List;

public interface RateService {

    Flux<Boolean> saveAll(List<RateDto> rates);

    Flux<RateDto> findAll();
}
