package com.sprih.event.service;

import com.sprih.event.model.EventType;
import com.sprih.event.model.dto.EventRequest;
import com.sprih.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    void testEventCreation() {
        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient", "test@test.com"));
        request.setCallbackUrl("http://localhost");

        String eventId = eventService.acceptEvent(request);

        assertNotNull(eventId);
    }
}
