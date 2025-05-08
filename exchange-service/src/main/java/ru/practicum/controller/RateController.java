package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.RateDto;
import ru.practicum.service.RateService;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;


    @PostMapping
    public Mono<ResponseEntity<Void>> receiveRates(@RequestBody List<RateDto> rates) {
        return rateService.saveAll(rates)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<RateDto>>> getRates() {
        return Mono.just(ResponseEntity.ok().body(rateService.findAll()));
    }

    @GetMapping("{currency}")
    public Mono<ResponseEntity<Mono<RateDto>>> getRate(@PathVariable String currency) {
        return Mono.just(ResponseEntity.ok().body(rateService.findByCurrency(currency)));
    }
}
