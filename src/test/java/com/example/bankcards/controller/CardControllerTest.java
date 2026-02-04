package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WithMockUser(roles = "USER")
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMyCards_ShouldReturnCardsPage_WhenValidParametersProvided() {
        // Given
        String status = "ACTIVE";
        String maskedNumber = "1234";
        Pageable pageable = PageRequest.of(0, 10);
        Page<CardDto> expectedPage = new PageImpl<>(List.of(new CardDto()));

        when(cardService.getCurrentUserCards(status, maskedNumber, pageable)).thenReturn(expectedPage);

        // When
        ResponseEntity<Page<CardDto>> response = cardController.getMyCards(status, maskedNumber, pageable);

        // Then
        assertEquals(expectedPage, response.getBody());
        verify(cardService, times(1)).getCurrentUserCards(status, maskedNumber, pageable);
    }

    @Test
    void getMyCard_ShouldReturnCard_WhenValidIdProvided() {
        // Given
        Long id = 1L;
        CardDto expectedCard = new CardDto();
        when(cardService.getCurrentUserCardById(id)).thenReturn(expectedCard);

        // When
        ResponseEntity<CardDto> response = cardController.getMyCard(id);

        // Then
        assertEquals(expectedCard, response.getBody());
        verify(cardService, times(1)).getCurrentUserCardById(id);
    }

    @Test
    void requestBlock_ShouldReturnBlockedCard_WhenValidIdProvided() {
        // Given
        Long id = 1L;
        CardDto expectedCard = new CardDto();
        when(cardService.requestBlockCard(id)).thenReturn(expectedCard);

        // When
        ResponseEntity<CardDto> response = cardController.requestBlock(id);

        // Then
        assertEquals(expectedCard, response.getBody());
        verify(cardService, times(1)).requestBlockCard(id);
    }

    @Test
    void getBalance_ShouldReturnCardWithBalance_WhenValidIdProvided() {
        // Given
        Long id = 1L;
        CardDto expectedCard = new CardDto();
        when(cardService.getCurrentUserCardById(id)).thenReturn(expectedCard);

        // When
        ResponseEntity<CardDto> response = cardController.getBalance(id);

        // Then
        assertEquals(expectedCard, response.getBody());
        verify(cardService, times(1)).getCurrentUserCardById(id);
    }
}
