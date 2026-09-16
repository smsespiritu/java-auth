package com.replit.authapi.service;

import com.replit.authapi.dto.AuthResponse;
import com.replit.authapi.dto.ForgotPasswordRequest;
import com.replit.authapi.dto.LoginRequest;
import com.replit.authapi.dto.MessageResponse;
import com.replit.authapi.dto.ResetPasswordRequest;
import com.replit.authapi.dto.SignupRequest;
import com.replit.authapi.exception.ApiException;
import com.replit.authapi.model.Role;
import com.replit.authapi.model.User;
import com.replit.authapi.repository.UserRepository;
import com.replit.authapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String PASSWORD_RESET_MESSAGE =
            "If an account exists for that email, a password reset token has been generated.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Transactional
    public MessageResponse signup(SignupRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "An account with that email already exists");
        }

        String verificationToken = UUID.randomUUID().toString();
        User user = new User(email, passwordEncoder.encode(request.password()), false, Set.of(Role.ROLE_USER));
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        emailService.sendVerificationEmail(email, verificationToken);
        return new MessageResponse("Account created. Check the Replit console for the verification token.");
    }

    @Transactional
    public MessageResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired verification token"));
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return new MessageResponse("Email verified. You can now log in.");
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password()));
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toUnmodifiableSet());
            return new AuthResponse(token, "Bearer", jwtService.getExpirationMs(), user.getEmail(), roles);
        } catch (AuthenticationException exception) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials or account is not verified");
        }
    }

    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmailIgnoreCase(normalizeEmail(request.email())).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
        return new MessageResponse(PASSWORD_RESET_MESSAGE);
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByPasswordResetToken(request.token())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired password reset token"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordResetToken(null);
        userRepository.save(user);
        return new MessageResponse("Password reset successfully.");
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}