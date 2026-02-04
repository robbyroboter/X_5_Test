package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Page<CardDto>> getMyCards(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String maskedNumber,
            @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(cardService.getCurrentUserCards(status, maskedNumber, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getMyCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCurrentUserCardById(id));
    }

    @PostMapping("/{id}/block-request")
    public ResponseEntity<CardDto> requestBlock(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.requestBlockCard(id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<CardDto> getBalance(@PathVariable Long id) {
        CardDto card = cardService.getCurrentUserCardById(id);
        return ResponseEntity.ok(card);
    }
}
