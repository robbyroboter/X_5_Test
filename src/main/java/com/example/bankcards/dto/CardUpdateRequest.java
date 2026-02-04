package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardUpdateRequest {

    private LocalDate expiryDate;

    private Card.CardStatus status;

    private BigDecimal balance;
}
