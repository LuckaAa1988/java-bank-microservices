package ru.practicum.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.exception.BlockerException;
import ru.practicum.service.BlockerService;

@Service
@RequiredArgsConstructor
public class BlockerServiceImpl implements BlockerService {
    @Override
    public Mono<Void> check(JsonNode jsonNode) {
        var username = jsonNode.get("username").asText();

        boolean isUnlucky = "Oleg".equalsIgnoreCase(username) || Math.random() < 0.1;

        if (isUnlucky) {
            return Mono.error(new BlockerException("Тебе не повезло"));
        }
        return Mono.empty();
    }
}
