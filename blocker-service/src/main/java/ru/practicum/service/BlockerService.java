package ru.practicum.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface BlockerService {
    Mono<Void> check(JsonNode jsonNode);
}
