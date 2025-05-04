package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {
    String currency;
    BigDecimal amount;
}
