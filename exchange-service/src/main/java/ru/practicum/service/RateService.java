package ru.practicum.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;

import java.util.List;
import java.util.Map;

public interface RateService {

    void saveAll(List<Map<String, Object>> rawRates);

    Flux<RateDto> findAll();

    Mono<RateDto> findByCurrency(String currency);
}
