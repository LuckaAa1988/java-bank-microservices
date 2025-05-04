package ru.practicum.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.practicum.model.entity.Account;

public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

    Flux<Account> findAllByUserId(Long userId);
}
