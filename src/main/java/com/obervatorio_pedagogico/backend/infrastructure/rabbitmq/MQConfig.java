package com.obervatorio_pedagogico.backend.infrastructure.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
    
    public static final String EXTRACAO_QUEUE_ENTRADA = "cobranca_queue_entrada";
    public static final String EXTRACAO_QUEUE_SAIDA = "cobranca_queue_saida";
    public static final String EXTRACAO_EXCHANGE = "extracao_exchange";
    public static final String ROUTING_KEY_ENTRADA = "extracao_routingKey_entrada";

    @Bean
    public Queue queueEntrada() {
        return new Queue(EXTRACAO_QUEUE_ENTRADA);
    }

    @Bean
    public DirectExchange exchange () {
        return new DirectExchange(EXTRACAO_EXCHANGE);
    }

    @Bean
    public Binding bindingExtracaoEntrada(Queue queue, DirectExchange exchange) {
        Binding binding = BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY_ENTRADA);
        return binding;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
