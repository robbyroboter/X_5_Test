package com.example.bankcards.controller;

import com.example.bankcards.dto.CardCreateRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardAdminControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardAdminController cardAdminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCard_ShouldReturnCreatedCard() {
        // Given
        CardCreateRequest request = new CardCreateRequest();
        CardDto expectedCardDto = new CardDto();
        when(cardService.createCard(request)).thenReturn(expectedCardDto);

        // When
        ResponseEntity<CardDto> response = cardAdminController.createCard(request);

        // Then
        assertEquals(expectedCardDto, response.getBody());
        verify(cardService, times(1)).createCard(request);
    }

    @Test
    void blockCard_ShouldReturnBlockedCard() {
        // Given
        Long cardId = 1L;
        CardDto expectedCardDto = new CardDto();
        when(cardService.blockCard(cardId)).thenReturn(expectedCardDto);

        // When
        ResponseEntity<CardDto> response = cardAdminController.blockCard(cardId);

        // Then
        assertEquals(expectedCardDto, response.getBody());
        verify(cardService, times(1)).blockCard(cardId);
    }

    @Test
    void activateCard_ShouldReturnActivatedCard() {
        // Given
        Long cardId = 1L;
        CardDto expectedCardDto = new CardDto();
        when(cardService.activateCard(cardId)).thenReturn(expectedCardDto);

        // When
        ResponseEntity<CardDto> response = cardAdminController.activateCard(cardId);

        // Then
        assertEquals(expectedCardDto, response.getBody());
        verify(cardService, times(1)).activateCard(cardId);
    }

    @Test
    void deleteCard_ShouldReturnNoContent() {
        // Given
        Long cardId = 1L;
        doNothing().when(cardService).deleteCard(cardId);

        // When
        ResponseEntity<Void> response = cardAdminController.deleteCard(cardId);

        // Then
        assertNull(response.getBody());
        verify(cardService, times(1)).deleteCard(cardId);
    }

    @Test
    void getAllCards_ShouldReturnEmptyList() {
        // When
        ResponseEntity<List<CardDto>> response = cardAdminController.getAllCards();

        // Then
        assertEquals(List.of(), response.getBody());
    }
}
