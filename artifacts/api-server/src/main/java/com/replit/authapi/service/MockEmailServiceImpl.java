package com.replit.authapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockEmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(MockEmailServiceImpl.class);

    @Override
    public void sendVerificationEmail(String email, String token) {
        log.info("MOCK EMAIL verification: email={}, verificationToken={}, link=/api/auth/verify-email?token={}",
                email, token, token);
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        log.info("MOCK EMAIL password reset: email={}, resetToken={}, link=/api/auth/reset-password",
                email, token);
    }
}