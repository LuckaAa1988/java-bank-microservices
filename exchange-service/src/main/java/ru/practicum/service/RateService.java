package ru.practicum.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;

import java.util.List;

public interface RateService {

    Flux<Boolean> saveAll(ConsumerRecord<String, List<RateDto>> rates);

    Flux<RateDto> findAll();

    Mono<RateDto> findByCurrency(String currency);
}
