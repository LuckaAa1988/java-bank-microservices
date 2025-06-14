package ru.practicum.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.client.AccountsClient;
import ru.practicum.client.BlockerClient;
import ru.practicum.client.ExchangeClient;
import ru.practicum.model.dto.*;
import ru.practicum.service.TransferService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountsClient accountsClient;
    private final BlockerClient blockerClient;
    private final ExchangeClient exchangeClient;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
    private final MeterRegistry meterRegistry;


    @Override
    public Mono<Void> internalTransfer(InternalTransferRequest internalTransferRequest) {
        return blockerClient.checkInternalTransfer(internalTransferRequest)
                .then(exchangeCurrency(
                        internalTransferRequest.getFromAccount(),
                        internalTransferRequest.getToAccount(),
                        internalTransferRequest.getAmount()
                ))
                .flatMap(convertedAmount -> {
                    var accountTransferRequest = AccountTransferRequest.builder()
                            .toUser(internalTransferRequest.getUsername())
                            .fromUser(internalTransferRequest.getUsername())
                            .fromAccount(internalTransferRequest.getFromAccount())
                            .toAccount(internalTransferRequest.getToAccount())
                            .depositAmount(convertedAmount)
                            .withdrawAmount(internalTransferRequest.getAmount())
                            .build();
                    return accountsClient.transfer(accountTransferRequest)
                            .then(Mono.fromRunnable(() ->
                                    kafkaTemplate.send("notifications",
                                            internalTransferRequest.getUsername(), NotificationDto.builder()
                                                    .username(internalTransferRequest.getUsername())
                                                    .data(String.format("Перевод со счёта %s на счёт %s на сумму %s успешно совершен!",
                                                            internalTransferRequest.getFromAccount(),
                                                            internalTransferRequest.getToAccount(),
                                                            internalTransferRequest.getAmount()))
                                                    .build()))).then();
                })
                .onErrorResume(error -> {
                    meterRegistry.counter("transfer_block",
                            "userFrom", internalTransferRequest.getUsername(),
                            "accountFrom", internalTransferRequest.getFromAccount(),
                            "userTo", internalTransferRequest.getUsername(),
                            "accountTo", internalTransferRequest.getToAccount()).increment();
                    return Mono.fromRunnable(() ->
                            kafkaTemplate.send("notifications",
                                    internalTransferRequest.getUsername(), NotificationDto.builder()
                                            .username(internalTransferRequest.getUsername())
                                            .data(String.format("Перевод со счёта %s на счёт %s на сумму %s заблокирован!",
                                                    internalTransferRequest.getFromAccount(),
                                                    internalTransferRequest.getToAccount(),
                                                    internalTransferRequest.getAmount()))
                                            .exception(error.getMessage())
                                            .build()));
                }).then();
    }

    @Override
    public Mono<Void> externalTransfer(ExternalTransferRequest externalTransferRequest) {
        return blockerClient.checkExternalTransfer(externalTransferRequest)
                .then(exchangeCurrency(
                        externalTransferRequest.getFromAccount(),
                        externalTransferRequest.getToAccount(),
                        externalTransferRequest.getAmount()
                ))
                .flatMap(convertedAmount -> {
                    var accountTransferRequest = AccountTransferRequest.builder()
                            .toUser(externalTransferRequest.getToUser())
                            .fromUser(externalTransferRequest.getUsername())
                            .fromAccount(externalTransferRequest.getFromAccount())
                            .toAccount(externalTransferRequest.getToAccount())
                            .depositAmount(convertedAmount)
                            .withdrawAmount(externalTransferRequest.getAmount())
                            .build();
                    return accountsClient.transfer(accountTransferRequest)
                            .then(Mono.fromRunnable(() ->
                                    kafkaTemplate.send("notifications",
                                            externalTransferRequest.getUsername(), NotificationDto.builder()
                                                    .username(externalTransferRequest.getUsername())
                                                    .data(String.format("Перевод со счёта %s на счёт %s (Пользователь: %s) на сумму %s успешно совершен!",
                                                            externalTransferRequest.getFromAccount(),
                                                            externalTransferRequest.getToAccount(),
                                                            externalTransferRequest.getToUser(),
                                                            externalTransferRequest.getAmount()))
                                                    .build()))).then();
                })
                .onErrorResume(error -> {
                    meterRegistry.counter("transfer_block",
                            "userFrom", externalTransferRequest.getUsername(),
                            "accountFrom", externalTransferRequest.getFromAccount(),
                            "userTo", externalTransferRequest.getToUser(),
                            "accountTo", externalTransferRequest.getToAccount()).increment();
                    return Mono.fromRunnable(() ->
                            kafkaTemplate.send("notifications",
                                    externalTransferRequest.getUsername(), NotificationDto.builder()
                                            .username(externalTransferRequest.getUsername())
                                            .data(String.format("Перевод со счёта %s на счёт %s (Пользователь: %s) на сумму %s заблокирован!",
                                                    externalTransferRequest.getFromAccount(),
                                                    externalTransferRequest.getToAccount(),
                                                    externalTransferRequest.getToUser(),
                                                    externalTransferRequest.getAmount()))
                                            .exception(error.getMessage())
                                            .build()));
                }).then();
    }

    public Mono<BigDecimal> exchangeCurrency(String from, String to, BigDecimal amount) {
        Mono<RateDto> rateFromMono = exchangeClient.getRate(from);
        Mono<RateDto> rateToMono = exchangeClient.getRate(to);

        if (from.equals("RUB") && to.equals("RUB")) {
            return Mono.just(amount);
        } else if (from.equals("RUB")) {
            return rateToMono.map(rateTo -> amount.multiply(rateTo.getRate()));
        } else if (to.equals("RUB")) {
            return rateFromMono.map(rateFrom -> amount.divide(rateFrom.getRate(), RoundingMode.HALF_UP));
        } else {
            return Mono.zip(rateFromMono, rateToMono)
                    .map(tuple -> {
                        var rateFrom = tuple.getT1();
                        var rateTo = tuple.getT2();
                        return amount
                                .divide(rateFrom.getRate(), RoundingMode.HALF_UP)
                                .multiply(rateTo.getRate());
                    });
        }
    }
}
