package com.sprih.event.model;

import java.time.Instant;
import java.util.Map;

public class Event {
    private String eventId;
    private EventType eventType;
    private Map<String, Object> payload;
    private String callbackUrl;
    private Instant createdAt;
}