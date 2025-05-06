package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountDto;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.dto.UserDto;
import ru.practicum.service.AccountService;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/users/{username}")
    public Mono<ResponseEntity<Flux<AccountResponse>>> getAll(@PathVariable String username) {
        return Mono.just(ResponseEntity.ok(accountService.getAll(username)));
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<AccountResponse>>> addAccount(@RequestBody @Valid UserDto userDto) {
        return Mono.just(ResponseEntity.ok(accountService.addAccount(userDto.getUsername(), userDto.getCurrency())));
    }

    @DeleteMapping
    public Mono<ResponseEntity<Mono<Void>>> deleteAccount(@RequestBody @Valid UserDto userDto) {
        return Mono.just(ResponseEntity.ok(accountService.deleteAccount(userDto.getUsername(), userDto.getCurrency())));
    }

    @PostMapping("/deposit")
    public Mono<ResponseEntity<Mono<Void>>> deposit(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(
                accountService.deposit(accountDto.getUsername(), accountDto.getCurrency(), accountDto.getAmount())));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<Mono<Void>>> withdraw(@RequestBody @Valid AccountDto accountDto) {
        return Mono.just(ResponseEntity.ok(
                accountService.withdraw(accountDto.getUsername(), accountDto.getCurrency(), accountDto.getAmount())));
    }
}
