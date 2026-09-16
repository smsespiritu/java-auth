package com.replit.authapi.repository;

import com.replit.authapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByVerificationToken(String verificationToken);
    Optional<User> findByPasswordResetToken(String passwordResetToken);
    boolean existsByEmailIgnoreCase(String email);
}