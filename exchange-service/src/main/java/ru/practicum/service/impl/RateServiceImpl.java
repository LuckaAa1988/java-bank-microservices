package ru.practicum.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mapper.RateMapper;
import ru.practicum.model.dto.RateDto;
import ru.practicum.model.entity.Rate;
import ru.practicum.service.RateService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateServiceImpl implements RateService {

    private static final String PREFIX = "rate:";
    private final ReactiveRedisTemplate<String, Rate> redisTemplate;
    private final RateMapper rateMapper;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "rates", groupId = "bank-kafka")
    public void saveAll(@Payload List<Map<String, Object>> rawRates) {
        List<RateDto> rates = rawRates.stream()
                .map(data -> objectMapper.convertValue(data, RateDto.class))
                .toList();

        Flux.fromIterable(rates)
                .flatMap(rateDto -> redisTemplate.opsForValue()
                        .set(PREFIX + rateDto.getCurrency(), rateMapper.fromDto(rateDto))).subscribe();
    }

    @Override
    public Flux<RateDto> findAll() {
        return redisTemplate.scan(ScanOptions.scanOptions().match("rate:*").build())
                .collectList()
                .flatMapMany(redisTemplate.opsForValue()::multiGet)
                .flatMap(Flux::fromIterable)
                .map(rateMapper::toDto)
                .filter(Objects::nonNull);
    }

    @Override
    public Mono<RateDto> findByCurrency(String currency) {
        return redisTemplate.opsForValue()
                .get(PREFIX + currency)
                .map(rateMapper::toDto);
    }
}
