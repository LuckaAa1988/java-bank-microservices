package ru.practicum.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExternalTransferRequest {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String toUser;
    @NotNull
    @NotBlank
    private String fromAccount;
    @NotNull
    @NotBlank
    private String toAccount;
    @NotNull
    @Positive
    private BigDecimal amount;
}
