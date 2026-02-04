package com.example.bankcards.service.impl;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    @Override
    @Transactional
    public TransferDto transferBetweenOwnCards(Long userId, TransferRequest request) {
        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        Card from = cardRepository.findByIdAndOwnerId(request.getFromCardId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("From card not found or not owned by user"));
        Card to = cardRepository.findByIdAndOwnerId(request.getToCardId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("To card not found or not owned by user"));

        if (from.getStatus() != Card.CardStatus.ACTIVE || to.getStatus() != Card.CardStatus.ACTIVE) {
            throw new IllegalStateException("Both cards must be ACTIVE");
        }

        BigDecimal amount = request.getAmount();
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        Transfer transfer = Transfer.builder()
                .fromCard(from)
                .toCard(to)
                .amount(amount)
                .status(Transfer.TransferStatus.COMPLETED)
                .build();

        Transfer saved = transferRepository.save(transfer);
        return mapToDto(saved);
    }

    private TransferDto mapToDto(Transfer transfer) {
        return TransferDto.builder()
                .id(transfer.getId())
                .fromCardId(transfer.getFromCard().getId())
                .toCardId(transfer.getToCard().getId())
                .amount(transfer.getAmount())
                .createdAt(transfer.getCreatedAt())
                .status(transfer.getStatus())
                .build();
    }
}
