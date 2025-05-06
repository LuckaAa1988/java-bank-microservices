package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountTransferRequest {
    private String fromUser;
    private String toUser;
    private String fromAccount;
    private String toAccount;
    private BigDecimal depositAmount;
    private BigDecimal withdrawAmount;
}
