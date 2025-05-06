package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.ExternalTransferRequest;
import ru.practicum.model.dto.InternalTransferRequest;
import ru.practicum.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/internal")
    public Mono<ResponseEntity<Mono<Void>>> internalTransfer(
            @RequestBody @Valid InternalTransferRequest internalTransferRequest) {
        return Mono.just(ResponseEntity.ok(transferService.internalTransfer(internalTransferRequest)));
    }

    @PostMapping("/external")
    public Mono<ResponseEntity<Mono<Void>>> externalTransfer(
            @RequestBody @Valid ExternalTransferRequest externalTransferRequest) {
        return Mono.just(ResponseEntity.ok(transferService.externalTransfer(externalTransferRequest)));
    }
}
