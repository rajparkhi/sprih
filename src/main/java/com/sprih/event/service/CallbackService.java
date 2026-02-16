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
        CallbackRequest body = CallbackRequest.success(event);
        restTemplate.postForEntity(event.getCallbackUrl(), body, Void.class);
    }

    public void sendFailure(Event event, String error) {
        CallbackRequest body = CallbackRequest.failure(event, error);
        restTemplate.postForEntity(event.getCallbackUrl(), body, Void.class);
    }
}

