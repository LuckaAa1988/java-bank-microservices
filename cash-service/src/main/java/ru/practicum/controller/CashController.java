package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.client.CashClient;
import ru.practicum.model.dto.AccountDto;


@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashClient cashClient;

    @PostMapping("/deposit")
    public Mono<ResponseEntity<Mono<Void>>> deposit(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(cashClient.deposit(accountDto)));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<Mono<Void>>> withdraw(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(cashClient.withdraw(accountDto)));
    }
}
