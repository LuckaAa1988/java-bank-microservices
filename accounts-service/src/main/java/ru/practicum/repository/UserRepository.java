package ru.practicum.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.practicum.model.entity.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername(String username);
    Mono<Boolean> deleteByUsername(String username);
}
