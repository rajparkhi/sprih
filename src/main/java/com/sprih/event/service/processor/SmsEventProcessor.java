package com.sprih.event.service.processor;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.service.CallbackService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SmsEventProcessor extends AbstractEventProcessor {

    public SmsEventProcessor(
            @Qualifier("smsQueue") BlockingQueue<Event> queue,
            CallbackService callbackService
    ) {
        super(queue, callbackService);
    }

    @Override
    protected int processingTimeSeconds() {
        return 3;
    }

    @Override
    protected EventType eventType() {
        return EventType.SMS;
    }
}

