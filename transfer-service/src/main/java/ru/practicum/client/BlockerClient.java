package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;

@Component
@RequiredArgsConstructor
public class BlockerClient {

    private final WebClient.Builder webclient;

    public Mono<Void> checkInternalTransfer(InternalTransferRequest internalTransferRequest) {
        return webclient.baseUrl("http://api-gateway:8080/api/blocker").build()
                .post()
                .bodyValue(internalTransferRequest)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> checkExternalTransfer(ExternalTransferRequest externalTransferRequest) {
        return webclient.baseUrl("http://api-gateway:8080/api/blocker").build()
                .post()
                .bodyValue(externalTransferRequest)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
