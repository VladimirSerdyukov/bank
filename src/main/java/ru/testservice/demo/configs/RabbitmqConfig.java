package ru.testservice.demo.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("pass");
        return connectionFactory;
    }

    @Bean
    public DirectExchange directExchangeGet(){
        return new DirectExchange("exchange-get", true, false);
    }
    @Bean
    public DirectExchange directExchangeUpdate(){
        return new DirectExchange("exchange-update", true, false);
    }

    @Bean
    Queue queueRequestGet(){
        return QueueBuilder.durable("queueRequestGet").maxLength(5000).build();
    }

    @Bean
    Queue queueRequestUpdate(){
        return QueueBuilder.durable("queueRequestUpdate").maxLength(5000).build();
    }

    @Bean
    public Binding bindingRequestGet() {
        return BindingBuilder.bind(queueRequestGet()).to(directExchangeGet()).with("request.get");
    }

    @Bean
    public Binding bindingRequestUpdate() {
        return BindingBuilder.bind(queueRequestUpdate()).to(directExchangeUpdate()).with("request.update");
    }

    @Bean
    Queue queueResponseGet(){
        return QueueBuilder.durable("queueResponseGet").maxLength(5000).build();
    }

    @Bean
    Queue queueResponseUpdate(){
        return QueueBuilder.durable("queueResponseUpdate").maxLength(5000).build();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.declareQueue(queueRequestGet());
        admin.declareQueue(queueResponseGet());
        admin.declareQueue(queueRequestUpdate());
        admin.declareQueue(queueResponseUpdate());
        return admin;
    }

}
