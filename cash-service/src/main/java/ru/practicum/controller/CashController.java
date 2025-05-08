package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountDto;
import ru.practicum.service.CashService;


@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping("/deposit")
    public Mono<ResponseEntity<Mono<Void>>> deposit(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(cashService.deposit(accountDto)));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<Mono<Void>>> withdraw(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(cashService.withdraw(accountDto)));
    }
}
