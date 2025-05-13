package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.UserLoginDto;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.service.impl.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public Mono<ResponseEntity<Void>> registration(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return authService.registration(userRegistrationDto)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

//    @PostMapping("/login")
//    public Mono<ResponseEntity<String>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
//        return authService.login(userLoginDto)
//                .then(Mono.just(ResponseEntity.ok().build()));
//    }
}
