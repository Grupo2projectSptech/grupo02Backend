package com.gestao.service;

import com.gestao.model.Venda;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaConsumer {

    @RabbitListener(queues = "${broker.queue.auditoria}")
    public void registrarAuditoria(Venda venda){
        System.out.println("[AuditoriaConsumer] Log de segurança registrado para a venda ID: " + venda.getId() + " - Produto " + venda.getProduto());
    }
}
