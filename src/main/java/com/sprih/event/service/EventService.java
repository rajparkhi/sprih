package com.sprih.event.service;

import com.sprih.event.model.Event;
import com.sprih.event.model.dto.EventRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EventService {


    private final BlockingQueue<Event> emailQueue;
    private final BlockingQueue<Event> smsQueue;
    private final BlockingQueue<Event> pushQueue;

    private volatile boolean accepting = true;

    public EventService(
            @Qualifier("emailQueue") BlockingQueue<Event> emailQueue,
            @Qualifier("smsQueue") BlockingQueue<Event> smsQueue,
            @Qualifier("pushQueue") BlockingQueue<Event> pushQueue) {
        this.emailQueue = emailQueue;
        this.smsQueue = smsQueue;
        this.pushQueue = pushQueue;
    }

    public String acceptEvent(EventRequest request) {
        if (!accepting) {
            throw new IllegalStateException("System shutting down");
        }

        Event event = new Event(
                UUID.randomUUID().toString(),
                request.getEventType(),
                request.getPayload(),
                request.getCallbackUrl(),
                Instant.now()
        );

        switch (event.getEventType()) {
            case EMAIL -> emailQueue.add(event);
            case SMS -> smsQueue.add(event);
            case PUSH -> pushQueue.add(event);
        }

        return event.getEventId();
    }

    public void stopAccepting() {
        accepting = false;
    }

    public BlockingQueue<Event> emailQueue() { return emailQueue; }
    public BlockingQueue<Event> smsQueue() { return smsQueue; }
    public BlockingQueue<Event> pushQueue() { return pushQueue; }
}
