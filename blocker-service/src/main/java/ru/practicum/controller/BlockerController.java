package ru.practicum.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.service.BlockerService;

@RestController
@RequestMapping("/api/blocker")
@RequiredArgsConstructor
public class BlockerController {

    private final BlockerService blockerService;

    @PostMapping
    public Mono<ResponseEntity<Mono<Void>>> check(@RequestBody JsonNode jsonNode) {
        return Mono.just(ResponseEntity.ok(blockerService.check(jsonNode)));
    }
}
