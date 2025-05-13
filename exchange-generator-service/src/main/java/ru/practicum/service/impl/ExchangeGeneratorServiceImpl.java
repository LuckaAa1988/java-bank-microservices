package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.practicum.client.GeneratorClient;
import ru.practicum.dto.RateDto;
import ru.practicum.service.ExchangeGeneratorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeGeneratorServiceImpl implements ExchangeGeneratorService {

    private final GeneratorClient generatorClient;

    @Override
    @Scheduled(fixedRate = 1000)
    public void generateRates() {
        var rates = List.of(RateDto.builder()
                        .currency("USD")
                        .rate(BigDecimal.valueOf(0.1 + (Math.random() * 0.1))
                                .setScale(4, RoundingMode.HALF_UP))
                        .build(),
                RateDto.builder()
                        .currency("CNY")
                        .rate(BigDecimal.valueOf(0.3 + (Math.random() * 0.1))
                                .setScale(4, RoundingMode.HALF_UP))
                        .build());
        generatorClient.send(rates);
    }
}
