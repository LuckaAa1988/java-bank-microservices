package ru.practicum.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.client.AccountsClient;
import ru.practicum.client.BlockerClient;
import ru.practicum.model.dto.AccountDto;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.service.CashService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final AccountsClient accountsClient;
    private final BlockerClient blockerClient;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    @Override
    public Mono<Void> deposit(AccountDto accountDto) {
        return blockerClient.checkDeposit(accountDto)
                .then(accountsClient.deposit(accountDto))
                .then(Mono.fromRunnable(() -> {
                            kafkaTemplate.send("notifications",
                                    accountDto.getUsername(), NotificationDto.builder()
                                            .username(accountDto.getUsername())
                                            .data(String.format("%s на счёт %s успешно зачислены!",
                                                    accountDto.getAmount(), accountDto.getCurrency()))
                                            .build());
                            log.info("Успешный депозит: username: {}, account: {}",
                                    accountDto.getUsername(), accountDto.getCurrency());
                        }
                ))
                .onErrorResume(error -> {
                    meterRegistry.counter("cash_block", "user", accountDto.getUsername(),
                            "account", accountDto.getCurrency()).increment();
                    log.error("Заблокирован депозит: username: {}, account: {}",
                            accountDto.getUsername(), accountDto.getCurrency());
                    return Mono.fromRunnable(() ->
                            kafkaTemplate.send("notifications", accountDto.getUsername(), NotificationDto.builder()
                                    .username(accountDto.getUsername())
                                    .data(String.format("Зачисление %s на счёт %s заблокировано!",
                                            accountDto.getAmount(), accountDto.getCurrency()))
                                    .exception(error.getMessage())
                                    .build()));
                }).then();
    }

    @Override
    public Mono<Void> withdraw(AccountDto accountDto) {
        return blockerClient.checkWithdraw(accountDto)
                .then(accountsClient.withdraw(accountDto))
                .then(Mono.fromRunnable(() -> {
                            kafkaTemplate.send("notifications",
                                    accountDto.getUsername(), (NotificationDto.builder()
                                            .username(accountDto.getUsername())
                                            .data(String.format("%s на счёт %s успешно сняты!",
                                                    accountDto.getAmount(), accountDto.getCurrency()))
                                            .build()));
                            log.info("Успешное снятие: username: {}, account: {}",
                                    accountDto.getUsername(), accountDto.getCurrency());
                        }
                        ))
                .onErrorResume(error -> {
                    meterRegistry.counter("cash_block", "user", accountDto.getUsername(),
                            "account", accountDto.getCurrency()).increment();
                    log.error("Заблокировано снятие: username: {}, account: {}",
                            accountDto.getUsername(), accountDto.getCurrency());
                    return Mono.fromRunnable(() ->
                            kafkaTemplate.send("notifications", accountDto.getUsername(), NotificationDto.builder()
                                    .username(accountDto.getUsername())
                                    .data(String.format("Снятие %s со счёта %s заблокировано!",
                                            accountDto.getAmount(), accountDto.getCurrency()))
                                    .exception(error.getMessage())
                                    .build()));
                }).then();
    }
}
