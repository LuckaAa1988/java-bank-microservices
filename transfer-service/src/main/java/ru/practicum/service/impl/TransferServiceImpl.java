package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.client.AccountsClient;
import ru.practicum.client.BlockerClient;
import ru.practicum.client.NotificationClient;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.service.TransferService;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountsClient accountsClient;
    private final BlockerClient blockerClient;
    private final NotificationClient notificationClient;

    @Override
    public Mono<Void> internalTransfer(InternalTransferRequest internalTransferRequest) {
        return blockerClient.checkInternalTransfer(internalTransferRequest)
                .flatMap(v -> accountsClient.internalTransfer(internalTransferRequest))
                .doOnSuccess(v -> notificationClient.sendSuccessNotification(NotificationDto.builder()
                        .data(internalTransferRequest)
                        .build()))
                .onErrorResume(error -> {
                    notificationClient.sendFailureNotification(NotificationDto.builder()
                            .data(internalTransferRequest)
                            .e(error)
                            .build());
                    return Mono.error(error);
                });
    }

    @Override
    public Mono<Void> externalTransfer(ExternalTransferRequest externalTransferRequest) {
        return blockerClient.checkExternalTransfer(externalTransferRequest)
                .flatMap(v -> accountsClient.externalTransfer(externalTransferRequest))
                .doOnSuccess(v -> notificationClient.sendSuccessNotification(NotificationDto.builder()
                        .data(externalTransferRequest)
                        .build()))
                .onErrorResume(error -> {
                    notificationClient.sendFailureNotification(NotificationDto.builder()
                            .data(externalTransferRequest)
                            .e(error)
                            .build());
                    return Mono.error(error);
                });
    }
}
