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

    @NotNull
    private EventType eventType;

    @NotNull
    private Map<String, Object> payload;

    @NotBlank
    private String callbackUrl;
}