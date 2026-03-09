package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Produto;
import com.gestao.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private EmpresaService empresaService;

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Produto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    public List<Produto> findByFornecedor(Long fornecedorId) {
        return repository.findByFornecedorId(fornecedorId);
    }

    public List<Produto> findByEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId);
    }

    public Produto save(Produto produto) {
        if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
            produto.setFornecedor(fornecedorService.findById(produto.getFornecedor().getId()));
        }
        if (produto.getEmpresa() != null && produto.getEmpresa().getId() != null) {
            produto.setEmpresa(empresaService.findById(produto.getEmpresa().getId()));
        }
        return repository.save(produto);
    }

    public Produto update(Long id, Produto data) {
        Produto produto = findById(id);
        produto.setNome(data.getNome());
        produto.setDescricao(data.getDescricao());
        produto.setPreco(data.getPreco());
        produto.setEstoque(data.getEstoque());
        produto.setCategoria(data.getCategoria());
        produto.setCodigoInterno(data.getCodigoInterno());
        produto.setUnidade(data.getUnidade());
        produto.setAtivo(data.isAtivo());
        if (data.getFornecedor() != null && data.getFornecedor().getId() != null) {
            produto.setFornecedor(fornecedorService.findById(data.getFornecedor().getId()));
        }
        if (data.getEmpresa() != null && data.getEmpresa().getId() != null) {
            produto.setEmpresa(empresaService.findById(data.getEmpresa().getId()));
        }
        return repository.save(produto);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
