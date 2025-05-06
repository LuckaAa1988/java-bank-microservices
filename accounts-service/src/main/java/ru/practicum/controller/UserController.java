package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.*;
import ru.practicum.service.AccountService;
import ru.practicum.service.UserService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @DeleteMapping
    public Mono<ResponseEntity<Void>> delete(@RequestBody @Valid UserDto userDto) {
        return userService.delete(userDto.getUsername())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PatchMapping
    public Mono<ResponseEntity<Void>> changePassword(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.changePassword(userLoginDto.getUsername(), userLoginDto.getPassword())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping
    public Mono<ResponseEntity<Void>> update(@RequestBody @Valid UserPatchDto userPatchDto) {
        return userService.update(userPatchDto)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/{username}")
    public Mono<ResponseEntity<Mono<UserResponse>>> getUser(@PathVariable String username) {
        return Mono.just(ResponseEntity.ok(userService.getUser(username)));
    }
}
