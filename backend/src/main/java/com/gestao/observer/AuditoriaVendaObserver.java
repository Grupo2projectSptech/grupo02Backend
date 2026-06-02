package com.gestao.observer;

import com.gestao.model.Venda;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Observer concreto — Auditoria de Vendas.
 *
 * Registra no log de auditoria cada venda concluída com os dados
 * financeiros calculados (margem, custo cheio, valor de venda).
 * Pode ser facilmente substituído por persistência em tabela de auditoria.
 */
@Component
public class AuditoriaVendaObserver implements VendaObserver {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaVendaObserver.class);

    @Autowired
    private VendaEventPublisher publisher;

    @PostConstruct
    public void init() {
        publisher.registrar(this);
    }

    @Override
    public void onVendaRegistrada(Venda venda) {
        log.info("""
                [AuditoriaVenda] ────────────────────────────────
                  Venda id      : {}
                  Produto       : {}
                  Plataforma    : {}
                  Quantidade    : {}
                  Valor venda   : R$ {}
                  Custo cheio   : R$ {}
                  Margem        : R$ {} ({}%)
                  ID Pedido     : {}
                ─────────────────────────────────────────────────""",
                venda.getId(),
                venda.getNomeProduto(),
                venda.getTipo(),
                venda.getQuantidade(),
                venda.getValorVenda(),
                venda.getCustoCheio(),
                venda.getMargem(),
                venda.getMargemPct(),
                venda.getIdPedido() != null ? venda.getIdPedido() : "—"
        );
    }
}
