package ru.practicum.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.exception.BlockerException;
import ru.practicum.service.BlockerService;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockerServiceImpl implements BlockerService {

    private final MeterRegistry meterRegistry;

    @Override
    public Mono<Void> check(JsonNode jsonNode) {
        var username = jsonNode.get("username").asText();

        boolean isUnlucky = "Oleg".equalsIgnoreCase(username) || Math.random() < 0.1;

        if (isUnlucky) {
            meterRegistry.counter("blocked", "user", username).increment();
            log.error("Заблокировано действие: username: {}", username);
            return Mono.error(new BlockerException("Тебе не повезло"));
        }
        return Mono.empty();
    }
}
