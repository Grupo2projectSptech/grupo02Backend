package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Fornecedor;
import com.gestao.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    public List<Fornecedor> findAll() {
        return repository.findAll();
    }

    public List<Fornecedor> findAtivos() {
        return repository.findByAtivoTrue();
    }

    public Fornecedor findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com id: " + id));
    }

    public Fornecedor save(Fornecedor fornecedor) {
        return repository.save(fornecedor);
    }

    public Fornecedor update(Long id, Fornecedor data) {
        Fornecedor fornecedor = findById(id);
        fornecedor.setNome(data.getNome());
        fornecedor.setCnpj(data.getCnpj());
        fornecedor.setContato(data.getContato());
        fornecedor.setEmail(data.getEmail());
        fornecedor.setTelefone(data.getTelefone());
        fornecedor.setEndereco(data.getEndereco());
        fornecedor.setCidade(data.getCidade());
        fornecedor.setEstado(data.getEstado());
        fornecedor.setCategoria(data.getCategoria());
        fornecedor.setAtivo(data.isAtivo());
        return repository.save(fornecedor);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
