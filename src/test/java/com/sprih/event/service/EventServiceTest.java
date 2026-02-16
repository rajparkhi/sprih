package com.sprih.event.service;


import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.model.dto.EventRequest;
import com.sprih.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    private BlockingQueue<Event> emailQueue;
    private BlockingQueue<Event> smsQueue;
    private BlockingQueue<Event> pushQueue;

    private EventService eventService;

    @BeforeEach
    void setup() {
        emailQueue = new LinkedBlockingQueue<>();
        smsQueue = new LinkedBlockingQueue<>();
        pushQueue = new LinkedBlockingQueue<>();

        eventService = new EventService(emailQueue, smsQueue, pushQueue);
    }

    @Test
    void testEmailEventAddedToCorrectQueue() {
        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient", "test@test.com"));
        request.setCallbackUrl("http://localhost");

        eventService.acceptEvent(request);

        assertEquals(1, emailQueue.size());
        assertEquals(0, smsQueue.size());
        assertEquals(0, pushQueue.size());
    }

    @Test
    void testFifoOrderPreserved() {
        EventRequest req1 = new EventRequest();
        req1.setEventType(EventType.EMAIL);
        req1.setPayload(Map.of());
        req1.setCallbackUrl("http://localhost");

        EventRequest req2 = new EventRequest();
        req2.setEventType(EventType.EMAIL);
        req2.setPayload(Map.of());
        req2.setCallbackUrl("http://localhost");

        String id1 = eventService.acceptEvent(req1);
        String id2 = eventService.acceptEvent(req2);

        Event first = emailQueue.poll();
        Event second = emailQueue.poll();

        assertEquals(id1, first.getEventId());
        assertEquals(id2, second.getEventId());
    }
}