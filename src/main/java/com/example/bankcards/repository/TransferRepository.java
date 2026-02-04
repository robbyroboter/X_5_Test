package com.example.bankcards.repository;

import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.Transfer.TransferStatus;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Page<Transfer> findByFromCardId(Long fromCardId, Pageable pageable);

    Page<Transfer> findByToCardId(Long toCardId, Pageable pageable);

    @Query("SELECT t FROM Transfer t WHERE t.fromCard.owner.id = :ownerId OR t.toCard.owner.id = :ownerId")
    Page<Transfer> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT t FROM Transfer t WHERE t.createdAt BETWEEN :from AND :to")
    Page<Transfer> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    List<Transfer> findByStatus(TransferStatus status);

    List<Transfer> findTop10ByFromCardOrderByCreatedAtDesc(Card fromCard);
}
