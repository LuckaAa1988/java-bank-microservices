package ru.practicum.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;

import java.util.List;

public interface RateService {

    Flux<Boolean> saveAll(List<RateDto> rates);

    Flux<RateDto> findAll();

    Mono<RateDto> findByCurrency(String currency);
}
