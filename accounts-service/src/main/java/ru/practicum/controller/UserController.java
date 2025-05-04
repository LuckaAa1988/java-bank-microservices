package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.model.dto.UserPatchDto;
import ru.practicum.model.dto.UserResponse;
import ru.practicum.service.AccountService;
import ru.practicum.service.UserService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users/{username}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @DeleteMapping
    public Mono<ResponseEntity<Void>> delete(@PathVariable String username) {
        return userService.delete(username)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PatchMapping
    public Mono<ResponseEntity<Void>> changePassword(@PathVariable String username,
                                                     @RequestParam @NotNull @NotBlank String password) {
        return userService.changePassword(username, password)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping
    public Mono<ResponseEntity<Void>> update(@PathVariable String username,
                                             @RequestBody @Valid UserPatchDto userPatchDto) {
        return userService.update(username, userPatchDto)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<Mono<UserResponse>>> getUser(@PathVariable String username) {
        return Mono.just(ResponseEntity.ok(userService.getUser(username)));
    }

    @GetMapping("/accounts")
    public Mono<ResponseEntity<Flux<AccountResponse>>> getAll(@PathVariable String username) {
        return Mono.just(ResponseEntity.ok(accountService.getAll(username)));
    }

    @PostMapping("/accounts/{currency}")
    public Mono<ResponseEntity<Mono<AccountResponse>>> addAccount(@PathVariable String username,
                                                                  @PathVariable String currency) {
        return Mono.just(ResponseEntity.ok(accountService.addAccount(username, currency)));
    }

    @DeleteMapping("/accounts/{currency}")
    public Mono<ResponseEntity<Mono<Void>>> deleteAccount(@PathVariable String username,
                                                          @PathVariable String currency) {
        return Mono.just(ResponseEntity.ok(accountService.deleteAccount(username, currency)));
    }

    @PostMapping("/deposit/{currency}")
    public Mono<ResponseEntity<Mono<Void>>> deposit(@PathVariable String username,
                                                               @PathVariable String currency,
                                                               @RequestParam BigDecimal amount) {
        return Mono.just(ResponseEntity.ok(accountService.deposit(username, currency, amount)));
    }

    @PostMapping("/withdraw/{currency}")
    public Mono<ResponseEntity<Mono<Void>>> withdraw(@PathVariable String username,
                                                                @PathVariable String currency,
                                                                @RequestParam BigDecimal amount) {
        return Mono.just(ResponseEntity.ok(accountService.withdraw(username, currency, amount)));
    }
}
