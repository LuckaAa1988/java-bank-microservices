package ru.practicum.service;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;

public interface TransferService {
    Mono<Void> internalTransfer(InternalTransferRequest internalTransferRequest);

    Mono<Void> externalTransfer(@Valid ExternalTransferRequest externalTransferRequest);
}
