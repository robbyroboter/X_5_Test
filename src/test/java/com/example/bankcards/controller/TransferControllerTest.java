package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransfer_ShouldReturnTransferDto_WhenValidRequest() {
        // Arrange
        Long userId = 1L;
        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        TransferDto expectedTransfer = new TransferDto();
        expectedTransfer.setId(1L);
        expectedTransfer.setFromCardId(1L);
        expectedTransfer.setToCardId(2L);
        expectedTransfer.setAmount(BigDecimal.valueOf(100));

        when(transferService.transferBetweenOwnCards(userId, request)).thenReturn(expectedTransfer);

        // Act
        ResponseEntity<TransferDto> response = transferController.createTransfer(request);

        // Assert
        assertEquals(expectedTransfer, response.getBody());
        verify(userService, times(1)).getCurrentUser();
        verify(transferService, times(1)).transferBetweenOwnCards(userId, request);
    }
}
