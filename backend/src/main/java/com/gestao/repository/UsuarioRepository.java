package com.gestao.repository;

import com.gestao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}
