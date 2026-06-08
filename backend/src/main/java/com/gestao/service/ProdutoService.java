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

    public Produto save(Produto produto) {
        if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
            produto.setFornecedor(fornecedorService.findById(produto.getFornecedor().getId()));
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
        return repository.save(produto);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
