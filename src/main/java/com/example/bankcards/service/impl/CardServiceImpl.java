package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardCreateRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberEncryptor;
import com.example.bankcards.util.CardNumberMasker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptor encryptor;
    @Override
    public CardDto createCard(CardCreateRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        String encrypted = encryptor.encrypt(request.getNumber());
        String masked = CardNumberMasker.mask(request.getNumber());

        Card card = Card.builder()
                .encryptedNumber(encrypted)
                .maskedNumber(masked)
                .owner(owner)
                .expiryDate(request.getExpiryDate().atEndOfMonth())
                .status(Card.CardStatus.ACTIVE)
                .balance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO)
                .build();

        Card saved = cardRepository.save(card);
        return mapToDto(saved);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public CardDto activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(Card.CardStatus.ACTIVE);
        return mapToDto(cardRepository.save(card));
    }

    @Override
    public CardDto blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(Card.CardStatus.BLOCKED);
        return mapToDto(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getCurrentUserCards(String status, String maskedNumber, Pageable pageable) {
        Long userId = getCurrentUserId();

        Card.CardStatus st = null;
        if (status != null) {
            st = Card.CardStatus.valueOf(status.toUpperCase(Locale.ROOT));
        }

        Page<Card> page;
        if (st != null && maskedNumber != null && !maskedNumber.isBlank()) {
            page = cardRepository.findByOwnerIdAndStatusAndMaskedNumberContainingIgnoreCase(
                    userId, st, maskedNumber, pageable);
        } else if (st != null) {
            page = cardRepository.findByOwnerIdAndStatus(userId, st, pageable);
        } else if (maskedNumber != null && !maskedNumber.isBlank()) {
            page = cardRepository.findByOwnerIdAndMaskedNumberContainingIgnoreCase(userId, maskedNumber, pageable);
        } else {
            page = cardRepository.findByOwnerId(userId, pageable);
        }

        return page.map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto getCurrentUserCardById(Long id) {
        Long userId = getCurrentUserId();
        Card card = cardRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found or does not belong to current user"));
        return mapToDto(card);
    }

    @Override
    public CardDto requestBlockCard(Long cardId) {
        Long userId = getCurrentUserId();
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found or does not belong to current user"));
        card.setStatus(Card.CardStatus.BLOCKED);
        Card saved = cardRepository.save(card);
        return mapToDto(saved);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
        return user.getId();
    }

    private CardDto mapToDto(Card card) {
        String ownerName = null;
        if (card.getOwner() != null) {
            ownerName = (card.getOwner().getFirstName() != null ? card.getOwner().getFirstName() : "") +
                    " " +
                    (card.getOwner().getLastName() != null ? card.getOwner().getLastName() : "");
        }
        return CardDto.builder()
                .id(card.getId())
                .maskedNumber(card.getMaskedNumber())
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .ownerName(ownerName != null ? ownerName.trim() : "")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAllCards(String status, String maskedNumber, Pageable pageable) {
        Card.CardStatus st = status != null ? Card.CardStatus.valueOf(status.toUpperCase()) : null;

        Page<Card> page;
        if (st != null && maskedNumber != null && !maskedNumber.isBlank()) {
            page = cardRepository.findByStatusAndMaskedNumberContainingIgnoreCase(st, maskedNumber, pageable);
        } else if (st != null) {
            page = cardRepository.findByStatus(st, pageable);
        } else if (maskedNumber != null && !maskedNumber.isBlank()) {
            page = cardRepository.findByMaskedNumberContainingIgnoreCase(maskedNumber, pageable);
        } else {
            page = cardRepository.findAll(pageable);
        }

        return page.map(this::mapToDto);
    }
}
