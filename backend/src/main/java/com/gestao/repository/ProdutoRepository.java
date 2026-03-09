package com.gestao.repository;

import com.gestao.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();
    List<Produto> findByFornecedorId(Long fornecedorId);
    List<Produto> findByEmpresaId(Long empresaId);
    List<Produto> findByCategoria(String categoria);
}
