package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;

@Component
@RequiredArgsConstructor
public class AccountsClient {

    public Mono<Void> internalTransfer(InternalTransferRequest internalTransferRequest) {
        return Mono.empty();
    }

    public Mono<Void> externalTransfer(ExternalTransferRequest externalTransferRequest) {
        return Mono.empty();
    }
}
