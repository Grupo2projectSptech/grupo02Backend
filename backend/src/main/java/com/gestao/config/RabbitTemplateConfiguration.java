package com.gestao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public Queue estoqueQueue() {
        return new Queue("venda.estoque.queue", true);
    }

    @Bean
    public Queue auditoriaQueue() {
        return new Queue("venda.auditoria.queue", true);
    }

    @Bean
    public Binding bindingEstoque(@Qualifier("estoqueQueue") Queue estoqueQueue, FanoutExchange vendaExchange) {
        return BindingBuilder
                .bind(estoqueQueue)
                .to(vendaExchange);
    }

    @Bean
    public Binding bindingAuditoria(@Qualifier("auditoriaQueue") Queue auditoriaQueue, FanoutExchange vendaExchange) {return  BindingBuilder
                .bind(auditoriaQueue)
                .to(vendaExchange);
    }



    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Essencial para datas no Java 8+
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    // 2. Configure a Factory do Listener para forçar o uso do conversor JSON
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter); // Define o conversor aqui!
        return factory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchangeVenda);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}