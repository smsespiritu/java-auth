package com.replit.authapi.config;

import com.replit.authapi.model.Role;
import com.replit.authapi.model.User;
import com.replit.authapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class DataInitializer {
    @Bean
    CommandLineRunner seedAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed-admin.enabled:false}") boolean enabled,
            @Value("${app.seed-admin.email:admin@example.com}") String email,
            @Value("${app.seed-admin.password:}") String password
    ) {
        return args -> {
            if (!enabled) {
                return;
            }
            if (password.isBlank()) {
                log.warn("Admin seeding is enabled but SEED_ADMIN_PASSWORD is empty; no admin was created.");
                return;
            }
            if (userRepository.existsByEmailIgnoreCase(email)) {
                return;
            }
            User admin = new User(email.trim().toLowerCase(), passwordEncoder.encode(password), true,
                    Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
            userRepository.save(admin);
            log.warn("Seeded configured admin account {}. Change or remove the seed credentials before production use.",
                    admin.getEmail());
        };
    }
}