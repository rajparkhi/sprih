package com.sprih.event.controller;

import com.sprih.event.model.dto.EventRequest;
import com.sprih.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest request) {
        String eventId = eventService.acceptEvent(request);
        return ResponseEntity.ok(
                Map.of("eventId", eventId, "message", "Event accepted for processing.")
        );
    }
}