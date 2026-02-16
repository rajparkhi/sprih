package com.sprih.event.model.dto;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventStatus;
import com.sprih.event.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackRequest {

    private String eventId;
    private EventStatus status;
    private EventType eventType;
    private String errorMessage;
    private Instant processedAt;

    public static CallbackRequest success(Event event) {
        return new CallbackRequest(
                event.getEventId(),
                EventStatus.COMPLETED,
                event.getEventType(),
                null,
                Instant.now()
        );
    }

    public static CallbackRequest failure(Event event, String errorMessage) {
        return new CallbackRequest(
                event.getEventId(),
                EventStatus.FAILED,
                event.getEventType(),
                errorMessage,
                Instant.now()
        );
    }
}
