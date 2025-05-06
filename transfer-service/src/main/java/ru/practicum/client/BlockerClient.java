package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;

@Component
@RequiredArgsConstructor
public class BlockerClient {

    public Mono<Void> checkInternalTransfer(InternalTransferRequest internalTransferRequest) {
        return Mono.empty();
    }

    public Mono<Void> checkExternalTransfer(ExternalTransferRequest externalTransferRequest) {
        return Mono.empty();
    }
}
