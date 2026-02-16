package com.sprih.event.config;

import com.sprih.event.model.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<Event> emailQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Event> smsQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Event> pushQueue() {
        return new LinkedBlockingQueue<>();
    }
}

