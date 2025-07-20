package com.practice.customerservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configurations for RabbitMQ
@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.queue}")
    private String queueName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Bean
    public Queue queue(){
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    @Bean("rabbitJsonTemplate")
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

}
