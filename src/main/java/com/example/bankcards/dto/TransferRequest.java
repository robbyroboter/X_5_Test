package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {

    @NotNull(message = "From card ID cannot be null")
    @Positive(message = "From card ID must be positive")
    private Long fromCardId;

    @NotNull(message = "To card ID cannot be null")
    @Positive(message = "To card ID must be positive")
    private Long toCardId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
}
