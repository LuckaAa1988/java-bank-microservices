package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.practicum.mapper.RateMapper;
import ru.practicum.model.dto.RateDto;
import ru.practicum.model.entity.Rate;
import ru.practicum.service.RateService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateServiceImpl implements RateService {

    private static final String PREFIX = "rate:";
    private final ReactiveRedisTemplate<String, Rate> redisTemplate;
    private final RateMapper rateMapper;

    @Override
    public Flux<Boolean> saveAll(List<RateDto> rates) {
        return Flux.fromIterable(rates)
                .flatMap(rateDto -> redisTemplate.opsForValue()
                        .set(PREFIX + rateDto.getCurrency(), rateMapper.fromDto(rateDto)));
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
}
