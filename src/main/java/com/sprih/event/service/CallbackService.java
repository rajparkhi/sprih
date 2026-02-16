package com.sprih.event.service;

import com.sprih.event.model.Event;
import com.sprih.event.model.EventType;
import com.sprih.event.model.dto.CallbackRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CallbackService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSuccess(Event event) {
        try {
            restTemplate.postForEntity(
                    event.getCallbackUrl(),
                    CallbackRequest.success(event),
                    Void.class
            );
        } catch (Exception e) {
            System.err.println("Callback failed: " + e.getMessage());
        }
    }

    public void sendFailure(Event event, String error) {
        try {
            restTemplate.postForEntity(
                    event.getCallbackUrl(),
                    CallbackRequest.failure(event, error),
                    Void.class
            );
        } catch (Exception e) {
            System.err.println("Callback failed: " + e.getMessage());
        }
    }
}

