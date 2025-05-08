package ru.practicum.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InternalTransferRequest {
    @NotNull
    @NotBlank
    private String username;
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
