package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.client.CashClient;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cash/{username}/{currency}")
@RequiredArgsConstructor
public class CashController {

    private final CashClient cashClient;

    @PostMapping("/deposit")
    public Mono<ResponseEntity<Mono<Void>>> deposit(@PathVariable String username,
                                                    @PathVariable String currency,
                                                    @RequestParam BigDecimal amount) {
        return Mono.just(ResponseEntity.ok(cashClient.deposit(username, currency, amount)));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<Mono<Void>>> withdraw(@PathVariable String username,
                                                     @PathVariable String currency,
                                                     @RequestParam BigDecimal amount) {
        return Mono.just(ResponseEntity.ok(cashClient.withdraw(username, currency, amount)));
    }
}
