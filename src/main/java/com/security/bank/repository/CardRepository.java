package com.security.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.security.bank.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("Select c from Card c where c.cardNumber = ?1")
    public Optional<Card> findByCardNumber(Long cardNumber);
}
