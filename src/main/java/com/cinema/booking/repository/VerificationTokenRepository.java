package com.cinema.booking.repository;

import com.cinema.booking.entities.authUserEntitiesSupport.VerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);
}