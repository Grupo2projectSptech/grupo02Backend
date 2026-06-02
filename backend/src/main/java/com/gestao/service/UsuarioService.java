package com.gestao.service;

import com.gestao.dto.RegisterRequestDTO;
import com.gestao.exception.BusinessException;
import com.gestao.model.Usuario;
import com.gestao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
        this.encoder    = new BCryptPasswordEncoder();
    }

    /**
     * Registra novo usuário a partir do DTO validado.
     * Lança BusinessException se o e-mail já estiver em uso.
     */
    public Usuario register(RegisterRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setName(dto.getName());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(encoder.encode(dto.getPassword()));
        usuario.setRole(dto.getRole() != null ? dto.getRole().toUpperCase() : "USER");

        return repository.save(usuario);
    }

    public Usuario findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
