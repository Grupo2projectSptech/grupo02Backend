package com.gestao.observer;

import com.gestao.model.Venda;
import com.gestao.service.VendaNotificador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class VendaEventPublisher implements VendaNotificador {

    private static final Logger log = LoggerFactory.getLogger(VendaEventPublisher.class);

    private final List<VendaObserver> observers = new ArrayList<>();

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.exchange.venda}")
    private String exchangeVenda;

    public void publicarVendaRealizada(Venda venda) {
        rabbitTemplate.convertAndSend(exchangeVenda, "", venda);
        System.out.println("Venda publicada no RabbitMQ: ID " + venda.getId() + "Enviada para a Exchange " + exchangeVenda);
    }

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

    @Override
    public void notificarNovaVenda(Venda venda) {

    }
}
