package com.sprih.event.model.dto;

import com.sprih.event.model.EventStatus;
import com.sprih.event.model.EventType;

import java.time.Instant;

public class CallbackRequest {

    private String eventId;
    private EventStatus status;
    private EventType eventType;
    private String errorMessage;
    private Instant processedAt;
}
