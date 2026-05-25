package com.gestao.config;

import com.gestao.model.Usuario;
import com.gestao.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadUsers(UsuarioRepository repository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (repository.findByEmail("admin") == null) {
                repository.save(new Usuario(null, "Administrador", "admin", encoder.encode("admin123"), "ADMIN"));
            }
            if (repository.findByEmail("gerente") == null) {
                repository.save(new Usuario(null, "Gerente", "gerente", encoder.encode("gerente123"), "GERENTE"));
            }
        };
    }
}
