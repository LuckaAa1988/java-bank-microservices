package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.client.BlockerClient;
import ru.practicum.client.AccountsClient;
import ru.practicum.client.NotificationClient;
import ru.practicum.model.dto.AccountDto;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.service.CashService;

@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final AccountsClient accountsClient;
    private final BlockerClient blockerClient;
    private final NotificationClient notificationClient;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Override
    public Mono<Void> deposit(AccountDto accountDto) {
        return blockerClient.checkDeposit(accountDto)
                .then(accountsClient.deposit(accountDto))
                .then(Mono.fromRunnable(() ->
                                kafkaTemplate.send("notifications",
                                        accountDto.getUsername(), NotificationDto.builder()
                        .username(accountDto.getUsername())
                        .data(String.format("%s на счёт %s успешно зачислены!",
                                accountDto.getAmount(), accountDto.getCurrency()))
                        .build()))
                .onErrorResume(error -> Mono.fromRunnable(() ->
                        kafkaTemplate.send("notifications", accountDto.getUsername(), NotificationDto.builder()
                                .username(accountDto.getUsername())
                                .data(String.format("Зачисление %s на счёт %s заблокировано!",
                                        accountDto.getAmount(), accountDto.getCurrency()))
                                .exception(error.getMessage())
                                .build())))).then();
    }

    @Override
    public Mono<Void> withdraw(AccountDto accountDto) {
        return blockerClient.checkWithdraw(accountDto)
                .then(accountsClient.withdraw(accountDto))
                .then(Mono.fromRunnable(() ->
                                kafkaTemplate.send("notifications",
                                        accountDto.getUsername(),(NotificationDto.builder()
                        .username(accountDto.getUsername())
                        .data(String.format("%s на счёт %s успешно сняты!",
                                accountDto.getAmount(), accountDto.getCurrency()))
                        .build())))
                .onErrorResume(error -> Mono.fromRunnable(() ->
                        kafkaTemplate.send("notifications", accountDto.getUsername(), NotificationDto.builder()
                                .username(accountDto.getUsername())
                                .data(String.format("Снятие %s со счёта %s заблокировано!",
                                        accountDto.getAmount(), accountDto.getCurrency()))
                                .exception(error.getMessage())
                                .build())))).then();
    }
}
