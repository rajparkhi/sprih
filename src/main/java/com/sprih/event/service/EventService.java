package com.sprih.event.service;

import com.sprih.event.model.Event;
import com.sprih.event.model.dto.EventRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EventService {

    private final BlockingQueue<Event> emailQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Event> smsQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Event> pushQueue = new LinkedBlockingQueue<>();

    private volatile boolean acceptingEvents = true;

    public String acceptEvent(EventRequest request) {
        if (!acceptingEvents) {
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
        acceptingEvents = false;
    }

    public BlockingQueue<Event> emailQueue() { return emailQueue; }
    public BlockingQueue<Event> smsQueue() { return smsQueue; }
    public BlockingQueue<Event> pushQueue() { return pushQueue; }
}
