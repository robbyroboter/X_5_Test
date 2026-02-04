package com.example.bankcards.service;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;

public interface TransferService {
    TransferDto transferBetweenOwnCards(Long userId, TransferRequest request);
}
