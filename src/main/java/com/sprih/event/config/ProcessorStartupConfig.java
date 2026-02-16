package com.sprih.event.config;

import com.sprih.event.service.EventService;
import com.sprih.event.service.processor.EmailEventProcessor;
import com.sprih.event.service.processor.PushEventProcessor;
import com.sprih.event.service.processor.SmsEventProcessor;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class ProcessorStartupConfig {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private final EventService eventService;
    private final EmailEventProcessor emailProcessor;
    private final SmsEventProcessor smsProcessor;
    private final PushEventProcessor pushProcessor;

    public ProcessorStartupConfig(
            EventService eventService,
            EmailEventProcessor emailProcessor,
            SmsEventProcessor smsProcessor,
            PushEventProcessor pushProcessor
    ) {
        this.eventService = eventService;
        this.emailProcessor = emailProcessor;
        this.smsProcessor = smsProcessor;
        this.pushProcessor = pushProcessor;

        executorService.submit(emailProcessor);
        executorService.submit(smsProcessor);
        executorService.submit(pushProcessor);
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {

        System.out.println("Graceful shutdown initiated...");

        // Stop accepting new events
        eventService.stopAccepting();

        // Signal processors to stop after draining queue
        emailProcessor.shutdown();
        smsProcessor.shutdown();
        pushProcessor.shutdown();

        // Stop executor from accepting new tasks
        executorService.shutdown();

        // Wait for running tasks to finish
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        System.out.println("All processors terminated cleanly.");
    }
}
