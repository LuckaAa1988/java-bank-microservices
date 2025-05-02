package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.RateDto;

import java.util.List;

@FeignClient(name = "exchange-service", url = "http://exchange-service:8082/api/rates")
public interface GeneratorClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void send(@RequestBody List<RateDto> rates);
}
