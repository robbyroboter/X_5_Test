package com.example.bankcards.dto;

import com.example.bankcards.entity.Transfer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {

    private Long id;

    private Long fromCardId;

    private Long toCardId;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private Transfer.TransferStatus status;
}
