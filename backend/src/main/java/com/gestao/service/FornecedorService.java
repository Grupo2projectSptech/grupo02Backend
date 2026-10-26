package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Fornecedor;
import com.gestao.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Page<Fornecedor> listarPaginado(Pageable pageable) {
        return fornecedorRepository.findAll(pageable);
    }


    public List<Fornecedor> findAll() {
        return fornecedorRepository.findAll();
    }

    public List<Fornecedor> findAtivos() {
        return fornecedorRepository.findByAtivoTrue();
    }

    public Fornecedor findById(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com id: " + id));
    }

    public Fornecedor save(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
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
        return fornecedorRepository.save(fornecedor);
    }

    public void delete(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor não encontrado com id: " + id);
        }
        fornecedorRepository.deleteById(id);
    }
}
