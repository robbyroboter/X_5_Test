package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateRequest;
import com.example.bankcards.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
    CardDto createCard(CardCreateRequest request);
    void deleteCard(Long cardId);
    CardDto activateCard(Long cardId);
    CardDto blockCard(Long cardId);
    Page<CardDto> getCurrentUserCards(String status, String maskedNumber, Pageable pageable);
    CardDto getCurrentUserCardById(Long id);
    CardDto requestBlockCard(Long cardId);
    Page<CardDto> getAllCards(String status, String maskedNumber, Pageable pageable);
}
