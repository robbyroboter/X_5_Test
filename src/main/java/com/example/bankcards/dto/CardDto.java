package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    private Long id;

    private String maskedNumber;

    private LocalDate expiryDate;

    private Card.CardStatus status;

    private BigDecimal balance;

    private String ownerName;
}
