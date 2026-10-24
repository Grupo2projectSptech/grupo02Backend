package com.gestao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitTemplateConfiguration {

    private final ConnectionFactory connectionFactory;

    @Value("${broker.exchange.venda}")
    private String exchangeVenda;

    @Value("${broker.queue.estoque}")
    private String queueEstoque;

    @Value("{broker.queue.auditoria}")
    private String queueAuditoria;

    @Bean
    public FanoutExchange vendaExchange() {
        return new FanoutExchange(exchangeVenda);
    }

    @Bean
    public Queue estoquequeue() {
        return new Queue(queueEstoque, true);
    }

    @Bean
    public Queue auditoriaqueue() {
        return new Queue(queueAuditoria, true);
    }

    @Bean
    public Binding bindingEstoque(Queue estoqueQueue, FanoutExchange vendaExchange) {
        return BindingBuilder
                .bind(estoqueQueue)
                .to(vendaExchange);
    }

    @Bean
    public Binding bindingAuditoria(Queue auditoriaQueue, FanoutExchange vendaExchange) {return  BindingBuilder
                .bind(auditoriaQueue)
                .to(vendaExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchangeVenda);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}