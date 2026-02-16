package com.sprih.event.model.dto;

import com.sprih.event.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotNull(message = "Event Type is required")
    private EventType eventType;

    @NotNull(message = "Payload cannot be null")
    private Map<String, Object> payload;

    @NotBlank(message = "Callback url is required")
    private String callbackUrl;
}