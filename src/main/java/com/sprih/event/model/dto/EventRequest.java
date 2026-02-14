package com.sprih.event.model.dto;

import com.sprih.event.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class EventRequest {

    @NotNull
    private EventType eventType;

    @NotNull
    private Map<String, Object> payload;

    @NotBlank
    private String callbackUrl;
}