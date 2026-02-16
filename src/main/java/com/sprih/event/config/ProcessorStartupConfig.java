package com.sprih.event.config;

import com.sprih.event.service.processor.EmailEventProcessor;
import com.sprih.event.service.processor.PushEventProcessor;
import com.sprih.event.service.processor.SmsEventProcessor;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorStartupConfig {

    private final Thread emailThread;
    private final Thread smsThread;
    private final Thread pushThread;

    public ProcessorStartupConfig(
            EmailEventProcessor emailProcessor,
            SmsEventProcessor smsProcessor,
            PushEventProcessor pushProcessor
    ) {
        this.emailThread = new Thread(emailProcessor);
        this.smsThread = new Thread(smsProcessor);
        this.pushThread = new Thread(pushProcessor);

        emailThread.start();
        smsThread.start();
        pushThread.start();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        emailThread.join();
        smsThread.join();
        pushThread.join();
    }
}