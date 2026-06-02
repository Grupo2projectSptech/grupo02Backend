package com.gestao.repository;

import com.gestao.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByDataBetween(LocalDate inicio, LocalDate fim);
    List<Venda> findByTipo(String tipo);
}
