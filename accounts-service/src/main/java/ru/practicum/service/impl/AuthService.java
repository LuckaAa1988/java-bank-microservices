package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.UserLoginDto;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.entity.User;
import ru.practicum.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ReactiveAuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> registration(UserRegistrationDto userRegistrationDto) {
        return userRepository.findByUsername(userRegistrationDto.getUsername())
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.save(User.builder()
                                .username(userRegistrationDto.getUsername())
                                .email(userRegistrationDto.getEmail())
                                .firstName(userRegistrationDto.getFirstName())
                                .lastName(userRegistrationDto.getLastName())
                                .birthDate(userRegistrationDto.getBirthDate())
                                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                                .build())
                ));
    }

    public Mono<Authentication> login(UserLoginDto userLoginDto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );
    }
}
