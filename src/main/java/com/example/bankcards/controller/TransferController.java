package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TransferController {

    private final TransferService transferService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<TransferDto> createTransfer(@RequestBody TransferRequest request) {
        var currentUser = userService.getCurrentUser();
        TransferDto transfer = transferService.transferBetweenOwnCards(currentUser.getId(), request);
        return ResponseEntity.ok(transfer);
    }
}
