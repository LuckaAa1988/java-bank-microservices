package ru.practicum.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDto {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String currency;
    @NotNull
    @Positive
    private BigDecimal amount;
}
