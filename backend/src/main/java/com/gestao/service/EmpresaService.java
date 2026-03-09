package com.gestao.service;

import com.gestao.exception.BusinessException;
import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Empresa;
import com.gestao.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    public List<Empresa> findAll() {
        return repository.findAll();
    }

    public Empresa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
    }

    public Empresa save(Empresa empresa) {
        if (repository.existsByCnpj(empresa.getCnpj())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ: " + empresa.getCnpj());
        }
        return repository.save(empresa);
    }

    public Empresa update(Long id, Empresa empresaData) {
        Empresa empresa = findById(id);
        if (!empresa.getCnpj().equals(empresaData.getCnpj()) && repository.existsByCnpj(empresaData.getCnpj())) {
            throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ: " + empresaData.getCnpj());
        }
        empresa.setRazaoSocial(empresaData.getRazaoSocial());
        empresa.setNomeFantasia(empresaData.getNomeFantasia());
        empresa.setCnpj(empresaData.getCnpj());
        empresa.setEmail(empresaData.getEmail());
        empresa.setTelefone(empresaData.getTelefone());
        empresa.setEndereco(empresaData.getEndereco());
        empresa.setCidade(empresaData.getCidade());
        empresa.setEstado(empresaData.getEstado());
        empresa.setCep(empresaData.getCep());
        empresa.setSegmento(empresaData.getSegmento());
        empresa.setAtivo(empresaData.isAtivo());
        return repository.save(empresa);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }
}
