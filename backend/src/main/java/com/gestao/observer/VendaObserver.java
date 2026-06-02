package com.gestao.observer;

import com.gestao.model.Venda;

/**
 * Interface Observer do padrão Observer (GoF).
 *
 * Qualquer classe que precise reagir ao evento de "venda registrada"
 * deve implementar esta interface e se registrar no {@link VendaEventPublisher}.
 */
public interface VendaObserver {

    /**
     * Chamado pelo publisher toda vez que uma venda é persistida com sucesso.
     *
     * @param venda a venda recém-registrada (já com id e campos calculados)
     */
    void onVendaRegistrada(Venda venda);
}
