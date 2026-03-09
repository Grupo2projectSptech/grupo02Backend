package com.gestao.repository;

import com.gestao.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByAtivoTrue();
    List<Fornecedor> findByCategoria(String categoria);
}
