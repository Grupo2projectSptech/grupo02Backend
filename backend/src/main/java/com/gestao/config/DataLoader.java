package com.gestao.config;

import com.gestao.model.Usuario;
import com.gestao.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class DataLoader {

    @Bean
    public CommandLineRunner loadUsers(UsuarioRepository repository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!repository.existsByEmail("admin@outletparty.com")) {
                repository.save(new Usuario(null, "Administrador", "admin@outletparty.com",
                        encoder.encode("admin123"), "ADMIN"));
            }
            if (!repository.existsByEmail("gerente@outletparty.com")) {
                repository.save(new Usuario(null, "Gerente", "gerente@outletparty.com",
                        encoder.encode("gerente123"), "GERENTE"));
            }
        };
    }
}
