package com.gestao.service;

import com.gestao.model.Venda;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EstoqueConsumer {

    @RabbitListener(queues = "${broker.queue.estoque}")
    public void abaterEstoque(Venda venda){
        System.out.println("[EstoqueConsumer] Venda recebida! Abatendo itens do estoque para a venda ID: " + venda.getId());
    }
}
