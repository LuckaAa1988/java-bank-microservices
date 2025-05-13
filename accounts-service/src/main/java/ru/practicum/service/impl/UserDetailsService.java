//package ru.practicum.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//import ru.practicum.repository.UserRepository;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailsService implements ReactiveUserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .switchIfEmpty(Mono.error(new RuntimeException("Пользователь не найден"))) //TODO
//                .map(user -> org.springframework.security.core.userdetails.User
//                        .withUsername(user.getUsername())
//                        .password(user.getPassword())
//                        .authorities("ROLE_USER")
//                        .build());
//    }
//}
