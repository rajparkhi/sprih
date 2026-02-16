package com.sprih.event.config;

import com.sprih.event.service.EventService;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfig {

    private final EventService eventService;
    private final EmailEventProcessor emailProcessor;
    private final SmsEventProcessor smsProcessor;
    private final PushEventProcessor pushProcessor;

    public ShutdownConfig(
            EventService eventService,
            EmailEventProcessor emailProcessor,
            SmsEventProcessor smsProcessor,
            PushEventProcessor pushProcessor
    ) {
        this.eventService = eventService;
        this.emailProcessor = emailProcessor;
        this.smsProcessor = smsProcessor;
        this.pushProcessor = pushProcessor;
    }

    @PreDestroy
    public void shutdown() {
        eventService.stopAccepting();
        emailProcessor.shutdown();
        smsProcessor.shutdown();
        pushProcessor.shutdown();
    }
}

