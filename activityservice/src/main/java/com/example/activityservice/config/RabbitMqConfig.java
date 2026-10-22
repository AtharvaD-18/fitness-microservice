package com.example.activityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration 
public class RabbitMqConfig {

    @Bean 
    public Queue activityQueue() {
        return new Queue("activity.queue", true);
    }

    @Bean
    public MessageConverter jsonMesasMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
