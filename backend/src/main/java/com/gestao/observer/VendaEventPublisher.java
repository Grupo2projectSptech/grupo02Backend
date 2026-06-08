package com.gestao.observer;

import com.gestao.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Publisher / Subject do padrão Observer (GoF).
 *
 * Mantém a lista de {@link VendaObserver}s registrados e os notifica
 * sempre que uma venda é registrada no sistema.
 *
 * Os observers são injetados automaticamente pelo Spring via
 * {@link #registrar(VendaObserver)} chamado em cada implementação
 * anotada com {@code @Component}.
 */
@Component
public class VendaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(VendaEventPublisher.class);

    private final List<VendaObserver> observers = new ArrayList<>();

    /** Registra um observador para receber eventos de venda. */
    public void registrar(VendaObserver observer) {
        observers.add(observer);
        log.info("[Observer] Registrado: {}", observer.getClass().getSimpleName());
    }

    /** Remove um observador previamente registrado. */
    public void remover(VendaObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observers registrados sobre uma nova venda.
     * Erros em um observer não impedem os demais de serem notificados.
     */
    public void notificar(Venda venda) {
        log.info("[Observer] Notificando {} observer(s) — venda id={}, produto='{}'",
                observers.size(), venda.getId(), venda.getNomeProduto());

        for (VendaObserver observer : observers) {
            try {
                observer.onVendaRegistrada(venda);
            } catch (Exception ex) {
                log.error("[Observer] Erro no observer {}: {}",
                        observer.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }
    }
}
