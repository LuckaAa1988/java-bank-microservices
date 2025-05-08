package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mapper.AccountMapper;
import ru.practicum.model.dto.AccountResponse;
import ru.practicum.repository.AccountRepository;
import ru.practicum.service.AccountService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final DatabaseClient databaseClient;

    @Override
    public Flux<AccountResponse> getAll(String username) {
        return databaseClient.sql("SELECT a.currency, a.amount FROM accounts AS a " +
                        "LEFT JOIN users AS u ON a.user_id = u.id WHERE u.username = :username")
                .bind("username", username)
                .map((row, metadata) -> AccountResponse.builder()
                        .currency(row.get("currency", String.class))
                        .amount(row.get("amount", BigDecimal.class))
                        .build())
                .all();
    }

    @Override
    public Mono<AccountResponse> addAccount(String username, String currency) {
        return databaseClient.sql("SELECT id FROM users WHERE username = :username")
                .bind("username", username)
                .map((row, metadata) -> row.get("id", Long.class))
                .one()
                .flatMap(id -> databaseClient.sql("INSERT INTO accounts (user_id, currency, amount) VALUES (:id, :currency, 0)")
                        .bind("id", id)
                        .bind("currency", currency)
                        .fetch()
                        .rowsUpdated()
                        .thenReturn(AccountResponse.builder()
                                .currency(currency)
                                .amount(BigDecimal.ZERO)
                                .build()));
    }

    @Override
    public Mono<Void> deleteAccount(String username, String currency) {
        return databaseClient.sql("DELETE FROM accounts WHERE user_id = " +
                "(SELECT id FROM users WHERE username=:username) AND currency = :currency AND amount = 0")
                .bind("username", username)
                .bind("currency", currency)
                .fetch()
                .rowsUpdated()
                .map(l -> {
                    if (l.intValue() == 0) {
                        return new RuntimeException("Нельзя удалить если есть средства"); //TODO
                    } else return 1;
                }).then();
    }

    @Override
    public Mono<Void> deposit(String username, String currency, BigDecimal amount) {
        return databaseClient.sql("UPDATE accounts SET amount = (amount + :amount) WHERE user_id = " +
                "(SELECT id FROM users WHERE username=:username) AND currency = :currency")
                .bind("amount", amount)
                .bind("username", username)
                .bind("currency", currency)
                .fetch()
                .rowsUpdated()
                .then();
    }

    @Override
    public Mono<Void> withdraw(String username, String currency, BigDecimal amount) {
        return databaseClient.sql("UPDATE accounts SET amount = (amount - :amount) WHERE user_id = " +
                        "(SELECT id FROM users WHERE username=:username) AND currency = :currency")
                .bind("amount", amount)
                .bind("username", username)
                .bind("currency", currency)
                .fetch()
                .rowsUpdated()
                .then();
    }

    @Override
    public Mono<Void> transfer(String fromUser,
                               String toUser,
                               String fromAccount,
                               String toAccount,
                               BigDecimal depositAmount,
                               BigDecimal withdrawAmount) {
        return databaseClient.inConnection(conn -> Mono.from(conn.beginTransaction())
                .then(databaseClient.sql("UPDATE accounts SET amount = amount - :withdrawAmount WHERE user_id = " +
                                "(SELECT id FROM users WHERE username=:fromUser) AND currency = :fromAccount")
                        .bind("withdrawAmount", withdrawAmount)
                        .bind("fromUser", fromUser)
                        .bind("fromAccount", fromAccount)
                        .fetch().rowsUpdated()
                )
                .then(databaseClient.sql("UPDATE accounts SET amount = amount + :depositAmount WHERE user_id = " +
                                "(SELECT id FROM users WHERE username=:toUser) AND currency = :toAccount")
                        .bind("depositAmount", depositAmount)
                        .bind("toUser", toUser)
                        .bind("toAccount", toAccount)
                        .fetch().rowsUpdated()
                )
                .then(Mono.from(conn.commitTransaction()))
        ).then();
    }
}
