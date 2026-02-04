package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Card.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Page<Card> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Card> findByOwnerIdAndStatus(Long ownerId, CardStatus status, Pageable pageable);

    Page<Card> findByOwnerIdAndMaskedNumberContainingIgnoreCase(Long ownerId, String maskedNumber, Pageable pageable);

    // Карты пользователя по статусу и маске
    @Query("SELECT c FROM Card c WHERE c.owner.id = :ownerId AND c.status = :status AND LOWER(c.maskedNumber) LIKE LOWER(CONCAT('%', :maskedNumber, '%'))")
    Page<Card> findByOwnerIdAndStatusAndMaskedNumberContainingIgnoreCase(
            @Param("ownerId") Long ownerId,
            @Param("status") CardStatus status,
            @Param("maskedNumber") String maskedNumber,
            Pageable pageable);

    Optional<Card> findByIdAndOwnerId(Long id, Long ownerId);

    Page<Card> findByStatus(Card.CardStatus status, Pageable pageable);
    Page<Card> findByStatusAndMaskedNumberContainingIgnoreCase(Card.CardStatus status, String maskedNumber, Pageable pageable);
    Page<Card> findByMaskedNumberContainingIgnoreCase(String maskedNumber, Pageable pageable);

}
