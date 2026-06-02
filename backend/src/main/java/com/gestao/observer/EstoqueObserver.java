package com.gestao.observer;

import com.gestao.model.Produto;
import com.gestao.model.Venda;
import com.gestao.repository.ProdutoRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Observer concreto — Controle de Estoque.
 *
 * Reage ao evento {@code onVendaRegistrada} decrementando o estoque
 * do {@link Produto} vinculado à venda. Se o produto não estiver
 * vinculado (venda manual sem produto cadastrado), o evento é ignorado.
 */
@Component
public class EstoqueObserver implements VendaObserver {

    private static final Logger log = LoggerFactory.getLogger(EstoqueObserver.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaEventPublisher publisher;

    /** Auto-registro no publisher ao inicializar o bean. */
    @PostConstruct
    public void init() {
        publisher.registrar(this);
    }

    @Override
    public void onVendaRegistrada(Venda venda) {
        if (venda.getProduto() == null) {
            log.debug("[EstoqueObserver] Venda id={} sem produto vinculado — estoque não alterado.", venda.getId());
            return;
        }

        produtoRepository.findById(venda.getProduto().getId()).ifPresent(produto -> {
            int estoqueAtual  = produto.getEstoque() == null ? 0 : produto.getEstoque();
            int qtdVendida    = venda.getQuantidade() == null ? 0 : venda.getQuantidade();
            int novoEstoque   = Math.max(0, estoqueAtual - qtdVendida);

            produto.setEstoque(novoEstoque);
            produtoRepository.save(produto);

            log.info("[EstoqueObserver] Produto '{}' (id={}) | estoque: {} → {}",
                    produto.getNome(), produto.getId(), estoqueAtual, novoEstoque);

            if (novoEstoque == 0) {
                log.warn("[EstoqueObserver] ⚠ Produto '{}' ficou SEM ESTOQUE após a venda id={}.",
                        produto.getNome(), venda.getId());
            }
        });
    }
}
