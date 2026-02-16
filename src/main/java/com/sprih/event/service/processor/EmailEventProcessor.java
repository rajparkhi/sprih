package com.sprih.event.service.processor;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.service.CallbackService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class EmailEventProcessor extends AbstractEventProcessor {

    public EmailEventProcessor(
            @Qualifier("emailQueue") BlockingQueue<Event> queue,
            CallbackService callbackService
    ) {
        super(queue, callbackService);
    }

    protected int processingTimeSeconds() { return 5; }
    protected EventType eventType() { return EventType.EMAIL; }
}
