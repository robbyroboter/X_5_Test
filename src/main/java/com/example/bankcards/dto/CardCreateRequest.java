package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardCreateRequest {

    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 19, message = "Card number must be 16 digits")
    @Pattern(regexp = "\\d{16}", message = "Card number must contain exactly 16 digits")
    private String number;

    @NotNull(message = "Expiry date cannot be null")
    private YearMonth expiryDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be greater than 0")
    private BigDecimal initialBalance;

    @NotNull(message = "Owner ID cannot be null")
    @Positive(message = "Owner ID must be positive")
    private Long ownerId;
}
